package pl.joannaz.culturalcentrewieliszew.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.joannaz.culturalcentrewieliszew.course.*;
import pl.joannaz.culturalcentrewieliszew.courseregistration.CourseRegistration;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseDetailsRepository courseDetailsRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void addUser(User user) {
        logger.info("Adding user {} {} to the database.", user.getFirstName(), user.getLastName());
        // set headshot
        user.setHeadshot(UserHelper.isFemale(user.getFirstName()) ? "assets/images/avatar4.svg" : "assets/images/avatar3.svg");
        this.userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        logger.info("Checking if user with username: {} exists.", username);
        return this.userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Fetching user with username: {}.", username);
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User with {} not found.", username);
                    return new UsernameNotFoundException(String.format("User with %s not found.", username));
                });
    }

    public List<User> getChildren(UUID parentId) {
        logger.info("Fetching children for user with id: {}.", parentId);
        return userRepository.findByParentId(parentId);
    }

    public User addChild(User parentUser, User childUser) {
        logger.info("Adding {} {} as a child of user {} {}.", childUser.getFirstName(), childUser.getLastName(),
                parentUser.getFirstName(), parentUser.getLastName());

        // Set the parent ID
        childUser.setParent(parentUser);

        // Set a contact phone as parent's phone
        childUser.setPhone(parentUser.getPhone());

        // Set a username based on parent's username
        childUser.setUsername(UserHelper.createChildUsername(parentUser.getUsername(), childUser.getFirstName(), childUser.getLastName()));

        // Set a headshot
        childUser.setHeadshot(UserHelper.generateChildHeadshotValue(childUser.getFirstName()));

        // Set a role
        childUser.setRole(Role.CLIENT);

        // Save the child user
        return userRepository.save(childUser);
    }

    public User updateChild (User updatedChild) {
        logger.info("Updating child: {} {} of user with id {}.",
                updatedChild.getFirstName(), updatedChild.getLastName(), updatedChild.getParent().getId());

        logger.info("Fetching original user.");
        User originalChild = userRepository.findById(updatedChild.getId())
                .orElseThrow(() -> {
                    logger.error("Error during fetching child: {} {}.", updatedChild.getFirstName(), updatedChild.getLastName());
                    return new EntityNotFoundException(
                            String.format("Child with id %s does not found.", updatedChild.getId())
                    );
                });

        logger.info("Checking whether child's first name or last name has been changed.");
        if(!Objects.equals(originalChild.getFirstName(), updatedChild.getFirstName())
        || !Objects.equals(originalChild.getLastName(), updatedChild.getLastName())) {
            originalChild.setFirstName(updatedChild.getFirstName());
            originalChild.setLastName(updatedChild.getLastName());

            // change username if updated one does not exist already
            String updatedUsername = UserHelper.updateChildUsername(
                    updatedChild.getUsername(), updatedChild.getFirstName(), updatedChild.getLastName());
            if (!this.existsByUsername(updatedUsername)) {originalChild.setUsername(updatedUsername);}

            // set headshot in case firstName has been changed (and it causes a change in the previously assumed gender)
            originalChild.setHeadshot(UserHelper.generateChildHeadshotValue(updatedChild.getFirstName()));
        }
        originalChild.setPhone(updatedChild.getPhone());
        originalChild.setDob(updatedChild.getDob());

        return userRepository.save(originalChild);
    }

    public User getUser (UUID userId) {
        logger.info("Fetching user with id: {}.", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("Error during fetching user with id: {}.", userId);
                    return new EntityNotFoundException(String.format("User with id %s not found.", userId));
                });
    }

    public void deleteChild (UUID childId) { // corresponding rows in CourseRegistration are deleted automatically (removing child from lists of participants)
        logger.info("Checking whether child with id: {} exists.", childId);
        boolean childExists = userRepository.existsById(childId);
        if (!childExists) {
            logger.error("Child with id: {} does not exist.", childId);
            throw new EntityNotFoundException(String.format("Child with id %s not found.", childId));
        }

        logger.info("Deleting child with id: {}", childId);
        userRepository.deleteById(childId);
    }

    public User getCurrentUser() {
        logger.info("Fetching current user from Security Context Holder.");
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public List<CourseBasicInfo> getCoursesForUser(UUID userId) {
        User user = this.getUser(userId);
        logger.info("Fetching courses for user with id: {}", userId);
        return user.getCourses().stream()
                .map(CourseRegistration::getCourse)
                .map(CourseBasicInfo::new)
                .toList();
    }

    @Transactional
    public void joinCourse(Long courseId, UUID userId) {
        // get course
        logger.info("Fetching course with id: {}", courseId);
        Course course = this.courseRepository.findById(courseId).orElseThrow(() -> {
            logger.error("Error during fetching course with id: {}", courseId);
            return new EntityNotFoundException(String.format("Course with id %s not found.", courseId));
        });

        // get course details to check if user age match minAge and maxAge values
        logger.info("Fetching course details with id: {} for {} course.", courseId, course.getName());
        CourseDetails courseDetails = courseDetailsRepository.findById(courseId).orElseThrow(() -> {
            logger.error("Error during fetching course details with id: {} for {} course.", courseId, course.getName());
            return new EntityNotFoundException(String.format("Course details with id %s not found.", courseId));
        });

        // get user
        logger.info("Fetching user with id: {}.", userId);
        User user = this.userRepository.findById(userId).orElseThrow(() -> {
            logger.error("Error during fetching user with id: {}", userId);
            return new EntityNotFoundException(String.format("User with id %s not found.", userId));
        });

        // check if there is free slot for new participant
        logger.info("Checking free slots for {} course.", course.getName());
        if (course.getParticipants().size() < course.getMaxParticipantsNumber()) {
            // check if user age match minAge and maxAge values
            logger.info("Validating age");
            if (isAgeCorrect(user.getDob(), courseDetails.getMinAge(), courseDetails.getMaxAge())) {
                // join course
                logger.info("Joining user {} {} to the {} course.", user.getFirstName(), user.getLastName(), course.getName());
                user.addCourse(course);
            }
            else {
                logger.error("User age does not match course's age constraints.");
                throw new RuntimeException("Sorry, your age does not fit within the age range of this course.");
            }
        }
        else {
            logger.error("There is no free slots for the {} course.", course.getName());
            throw new RuntimeException("Sorry, there is no more available slots for this course.");
        }
    }

    public boolean isAgeCorrect(LocalDate dob, int minAge, int maxAge) {
        int age = Period.between(dob, LocalDate.now()).getYears();
        logger.info("User age: {}, course min age: {}, course max age: {}", age, minAge, maxAge);
        return age >= minAge && age <= maxAge;
    }

    @Transactional
    public void removeCourse(Long courseId, UUID userId) {
        logger.info("Fetching course with id: {}", courseId);
        Course course = this.courseRepository.findById(courseId).orElseThrow(() -> {
            logger.error("Error during fetching course with id: {}", courseId);
            return new EntityNotFoundException(String.format("Course with id %s not found.", courseId));
        });

        logger.info("Fetching user with id: {}", userId);
        User user = this.userRepository.findById(userId).orElseThrow(() -> {
            logger.error("Error during fetching user with id: {}", userId);
            return new EntityNotFoundException(String.format("User with id %s not found.", userId));
        });

        logger.info("Removing {} course for user with id: {}", course.getName(), user.getId());
        user.removeCourse(course);
    }

    public List<UserBasicInfo> findTeachers() {
        logger.info("Fetching all teachers");
        return userRepository.findTeachers();
    }

    public List<EmployeeProfile> findEmployees() {
        logger.info("Fetching all employees");
        return userRepository.findEmployees();
    }

    public List<UserBasicInfo> getChildrenSimpleData(UUID id) {
        logger.info("Fetching children for user with id: {}.", id);
        return userRepository.findChildrenBasicInfo(id);
    }

    public EmployeeProfile getEmployeeById(UUID id) {
        logger.info("Fetching employee with id: {}.", id);
        User employee = this.userRepository.findById(id).orElseThrow(() -> {
            logger.error("Error during fetching employee with id: {}", id);
            return new EntityNotFoundException(String.format("Employee with id %s not found.", id));
        });

        return new EmployeeProfile(id, employee.getFirstName(), employee.getLastName(),
                employee.getHeadshot(), employee.getPosition(), employee.getDescription());
    }

    @Transactional
    public void deleteEmployee(UUID employeeId) {
        logger.info("Fetching employee with id: {}.", employeeId);
        User employee = this.userRepository.findById(employeeId).orElseThrow(() -> {
            logger.error("Error during fetching employee with id: {}", employeeId);
            return new EntityNotFoundException(String.format("Employee with id %s not found.", employeeId));
        });

        if(employee.getPosition().equals("Teacher")) { // get teacher courses and set teacher value to null
            logger.info("Fetching courses led by teacher: {} {}.", employee.getFirstName(), employee.getLastName());
            List<Course> courses = this.courseRepository.findTeacherCourses(employeeId);
            logger.info("Removing teacher for all courses led by: {} {}.", employee.getFirstName(), employee.getLastName());
            courses.forEach(course -> course.setTeacher(null));
        }

        logger.info("Deleting employee with id: {}.", employeeId);
        userRepository.deleteById(employeeId);
    }

    public User addEmployee(UserDTO employee) {
        logger.info("Adding employee: {} {} to the database.", employee.getFirstName(), employee.getLastName());
        User newEmployee = UserHelper.createEmployee(employee);
        return userRepository.save(newEmployee);
    }

    @Transactional
    public User updateEmployee(UserDTO updatedEmployee) {
        logger.info("Fetching employee with id: {}.", updatedEmployee.getId());
        User originalEmployee = userRepository.findById(updatedEmployee.getId()).orElseThrow(() -> {
            logger.error("Error during fetching employee with id: {}", updatedEmployee.getId());
            return new EntityNotFoundException(String.format("Employee with id %s not found.", updatedEmployee.getId()));
        });

        originalEmployee.setFirstName(updatedEmployee.getFirstName());
        originalEmployee.setLastName(updatedEmployee.getLastName());
        originalEmployee.setPhone(updatedEmployee.getPhone());
        originalEmployee.setDob(updatedEmployee.getDob());
        originalEmployee.setRole(updatedEmployee.getRole());
        originalEmployee.setPosition(updatedEmployee.getPosition());
        originalEmployee.setDescription(updatedEmployee.getDescription());

        logger.info("Saving updated employee: {} {}.", originalEmployee.getFirstName(), originalEmployee.getLastName());
        return this.userRepository.save(originalEmployee);
    }

    @Transactional
    public User updateClient(UserDTO updatedClient) {
        User originalClient = this.getCurrentUser();

        originalClient.setFirstName(updatedClient.getFirstName());
        originalClient.setLastName(updatedClient.getLastName());
        originalClient.setPhone(updatedClient.getPhone());
        originalClient.setDob(updatedClient.getDob());

        logger.info("Saving updated client: {} {}.", updatedClient.getFirstName(), updatedClient.getLastName());
        return this.userRepository.save(originalClient);
    }

    @Transactional
    public void deleteClientAccount () { // only for Role.CLIENT
        User currentUser = this.getCurrentUser();

        // check if user has children and remove children accounts
        List<User> children = this.getChildren(currentUser.getId());
        logger.info("Removing children for user with id: {}.", currentUser.getId());
        children.forEach(child -> this.deleteChild(child.getId()));

        // remove user account (corresponding rows in CourseRegistration should be deleted automatically)
        logger.info("Removing account of client with id: {}.", currentUser.getId());
        this.userRepository.deleteById(currentUser.getId());
    }
}
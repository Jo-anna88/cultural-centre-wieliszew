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
import pl.joannaz.culturalcentrewieliszew.linkEntities.UserCourse;

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
        childUser.setParentId(parentUser.getId());

        // Set a contact phone as parent's phone
        childUser.setPhone(parentUser.getPhone());

        // Set a username based on parent's username
        childUser.setUsername(UserHelper.createChildUsername(parentUser.getUsername(), childUser.getFirstName(), childUser.getLastName()));

        // Set a headshot
        childUser.setHeadshot(UserHelper.generateChildHeadshotValue(childUser.getFirstName()));

        // Save the child user
        return userRepository.save(childUser);
    }

    public User updateChild (User updatedChild) {
        logger.info("Updating child: {} {} of user with id {}.",
                updatedChild.getFirstName(), updatedChild.getLastName(), updatedChild.getParentId());

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

    public void deleteChild (UUID childId) { // corresponding rows in UserCourse are deleted automatically (removing child from lists of participants)
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
                .map(UserCourse::getCourse)
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
        logger.info("Checking whether there are free slots for {} course.", course.getName());
        if (course.getParticipants().size() < course.getMaxParticipantsNumber()) {
            logger.info("Validating age");
            // check if user age match minAge and maxAge values
            if (isAgeCorrect(user.getDob(), courseDetails.getMinAge(), courseDetails.getMaxAge())) {
                // join course
                user.addCourse(course);
            }
            else { throw new RuntimeException("Sorry, your age does not fit within the age range of this course."); }
        }
        else { throw new RuntimeException("Sorry, there is no more available slots for this course."); }
    }

    public boolean isAgeCorrect(LocalDate dob, int minAge, int maxAge) {
        int age = Period.between(dob, LocalDate.now()).getYears();
        return age >= minAge && age <= maxAge;
    }

    @Transactional
    public void removeCourse(Long courseId, UUID userId) {
        Course course = this.courseRepository.findById(courseId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Course with id %s not found.", courseId))
        );
        User user = this.userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(String.format("User with id %s not found.", userId))
        );
        user.removeCourse(course);
    }

    public List<UserBasicInfo> findTeachers() {
        return userRepository.findTeachers();
    }

    public List<EmployeeProfile> findEmployees() {
        return userRepository.findEmployees();
    }

    public List<UserBasicInfo> getChildrenSimpleData(UUID id) {
        return userRepository.findChildrenBasicInfo(id);
    }

    public EmployeeProfile getEmployeeById(UUID id) {
        User employee = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Employee with id %s does not exist.", id)
                ));
        return new EmployeeProfile(id, employee.getFirstName(), employee.getLastName(),
                employee.getHeadshot(), employee.getPosition(), employee.getDescription());
    }

    @Transactional
    public void deleteEmployee(UUID employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Employee with id %s does not exist.", employeeId)
                ));
        if(employee.getPosition().equals("Teacher")) { // get teacher courses and set teacher value to null
            List<Course> courses = this.courseRepository.findTeacherCourses(employeeId);
            courses.forEach(course -> course.setTeacher(null));
        }
        userRepository.deleteById(employeeId);
    }

    public User addEmployee(UserDTO employee) {
        User newEmployee = UserHelper.createEmployee(employee);
        return userRepository.save(newEmployee);
    }

    @Transactional
    public User updateEmployee(UserDTO updatedEmployee) {
        User originalEmployee = userRepository.findById(updatedEmployee.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Employee with this id does not exist."
                ));
        originalEmployee.setFirstName(updatedEmployee.getFirstName());
        originalEmployee.setLastName(updatedEmployee.getLastName());
        originalEmployee.setPhone(updatedEmployee.getPhone());
        originalEmployee.setDob(updatedEmployee.getDob());
        originalEmployee.setRole(updatedEmployee.getRole());
        originalEmployee.setPosition(updatedEmployee.getPosition());
        originalEmployee.setDescription(updatedEmployee.getDescription());
        return this.userRepository.save(originalEmployee);
    }

    @Transactional
    public User updateClient(UserDTO updatedClient) { // todo: what about: 1. username - no!, 2. children username - no!, 3. children contact phone - only if child contact phone was parent's phone
        User originalClient = this.getCurrentUser();
        originalClient.setFirstName(updatedClient.getFirstName());
        originalClient.setLastName(updatedClient.getLastName());
        originalClient.setPhone(updatedClient.getPhone());
        originalClient.setDob(updatedClient.getDob());
        return this.userRepository.save(originalClient);
    }

    @Transactional
    public void deleteClientAccount () { // only for Role.CLIENT
        User currentUser = this.getCurrentUser();

        // check if user has children and remove children accounts
        List<User> children = this.getChildren(currentUser.getId());
        children.forEach(child -> this.deleteChild(child.getId()));

        // remove user account (corresponding rows in UserCourse should be deleted automatically)
        this.userRepository.deleteById(currentUser.getId());
    }
}

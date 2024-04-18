package pl.joannaz.culturalcentrewieliszew.user;

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
        // set headshot
        user.setHeadshot(UserHelper.isFemale(user.getFirstName()) ? "assets/images/avatar4.svg" : "assets/images/avatar3.svg");
        this.userRepository.save(user);
    }

    public Optional<User> findUserByUsername(String username) {return this.userRepository.findByUsername(username);}
    public boolean existsByUsername(String username) {return this.userRepository.existsByUsername(username);}
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found in the database."));
    }
    public List<User> getChildren(UUID parentId) {
        return userRepository.findByParentId(parentId);
    }
    public User addChild(User parentUser, User childUser) {
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
        User originalChild = userRepository.findById(updatedChild.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Child with this id does not exist."
                ));

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
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("User with id %s does not exist.", userId)
                ));
    }

    public void deleteChild (UUID childId) { // corresponding rows in UserCourse are deleted automatically (removing child from lists of participants)
        userRepository.deleteById(childId);
    }

    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public List<CourseBasicInfo> getCoursesForUser(UUID userId) {
        User user = this.getUser(userId);
        return user.getCourses().stream()
                .map(UserCourse::getCourse)
                .map(CourseBasicInfo::new)
                .toList();
    }

    @Transactional
    public void addCourse(Long courseId) {
            Course course = this.courseRepository.findById(courseId).orElseThrow(() ->
                    new NoSuchElementException(String.format("Course with id %s not found.", courseId))
            );
//        CourseDetails courseDetails = courseDetailsRepository.findById(course.getId()).orElseThrow(() ->
//                new NoSuchElementException(String.format("Course details with id %s not found.", courseId))
//        );

            if (course.getParticipants().size() < course.getMaxParticipantsNumber()) {
                User currentUser = getCurrentUser();
//            if (isAgeCorrect(currentUser.getDob(), courseDetails.getMinAge(), courseDetails.getMaxAge()))
                currentUser.addCourse(course);
//            else throw new RuntimeException("Sorry, your age does not fit within the age range of this course.");
            } else {
                throw new RuntimeException("Sorry, there is no more available slots for this course.");
            }
    }

    @Transactional
    public void joinCourse(Long courseId, UUID userId) {
        // get course
        Course course = this.courseRepository.findById(courseId).orElseThrow(() ->
                new NoSuchElementException(String.format("Course with id %s not found.", courseId))
        );
        // get course details to check if user age match minAge and maxAge values
        CourseDetails courseDetails = courseDetailsRepository.findById(courseId).orElseThrow(() ->
                new NoSuchElementException(
                        String.format("Course Details with id %s not found.", courseId)
                ));
        // get user
        User user = this.userRepository.findById(userId).orElseThrow(() ->
                new NoSuchElementException(String.format("User with id %s not found.", userId))
        );

        // check if there is free slot for new participant
        if (course.getParticipants().size() < course.getMaxParticipantsNumber()) {
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
                new NoSuchElementException(String.format("Course with id %s not found.", courseId))
        );
        User user = this.userRepository.findById(userId).orElseThrow(() ->
                new NoSuchElementException(String.format("User with id %s not found.", userId))
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
                .orElseThrow(() -> new IllegalStateException(
                        String.format("Employee with id %s does not exist.", id)
                ));
        return new EmployeeProfile(id, employee.getFirstName(), employee.getLastName(),
                employee.getHeadshot(), employee.getPosition(), employee.getDescription());
    }

    @Transactional
    public void deleteEmployee(UUID employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalStateException(
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
                .orElseThrow(() -> new IllegalStateException(
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

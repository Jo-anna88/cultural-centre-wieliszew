package pl.joannaz.culturalcentrewieliszew.user;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.joannaz.culturalcentrewieliszew.course.*;
import pl.joannaz.culturalcentrewieliszew.linkEntities.UserCourse;

import java.util.*;

@Service
public class UserService implements UserDetailsService { //interface UserService ?
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseDetailsRepository courseDetailsRepository;

    public UserService (UserRepository userRepository, CourseRepository courseRepository, CourseDetailsRepository courseDetailsRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.courseDetailsRepository = courseDetailsRepository;
    }
    //private final User user;
    //User saveUser(User user); ?
    public User addUser(User user) {
        // set headshot
        user.setHeadshot(UserHelper.isFemale(user.getFirstName()) ? "assets/images/avatar4.svg" : "assets/images/avatar3.svg");

        return this.userRepository.save(user);
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
        childUser.setHeadshot(UserHelper.isFemale(childUser.getFirstName()) ? "assets/images/avatar-girl.svg" : "assets/images/avatar-boy.svg");

        // Save the child user
        return userRepository.save(childUser);
    }

    public User updateChild (User updatedChild) {
        User originalChild = userRepository.findById(updatedChild.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Child with this id does not exist."
                ));
        originalChild.setFirstName(updatedChild.getFirstName());
        originalChild.setLastName(updatedChild.getLastName());
        originalChild.setPhone(updatedChild.getPhone());
        originalChild.setDob(updatedChild.getDob());
        return userRepository.save(originalChild);
    }

    public User getUser (UUID userId) {
        return userRepository.findById(userId).get(); // todo: check isPresent()
    }

    public void deleteChild (UUID childId) {
        userRepository.deleteById(childId); // todo: courses list should be deleted too?
    }

    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    }

    public List<CourseDTO> getCoursesForUser(UUID userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<CourseDTO> result = user.getCourses().stream()
                    .map(UserCourse::getCourse)
                    .map(CourseDTO::new)
                    .toList();
            return result;
        } else {
            // todo: Handle the case where user with the given ID is not found
            return Collections.emptyList();
        }
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
    public void joinCourseByUserId(Long courseId, UUID userId) {
        Course course = this.courseRepository.findById(courseId).orElseThrow(() ->
                new NoSuchElementException(String.format("Course with id %s not found.", courseId))
        );
        User user = this.userRepository.findById(userId).orElseThrow(() ->
                new NoSuchElementException(String.format("User with id %s not found.", userId))
        );
        if (course.getParticipants().size() < course.getMaxParticipantsNumber()) {
//            if (isAgeCorrect(child.getDob(), courseDetails.getMinAge(), courseDetails.getMaxAge()))
            user.addCourse(course);
//            else throw new RuntimeException("Sorry, your age does not fit within the age range of this course.");
        }
        else {
            throw new RuntimeException("Sorry, there is no more available slots for this course.");
        }
    }

//    public boolean isAgeCorrect(LocalDate dob, int minAge, int maxAge) {
//        int age = Period.between(dob, LocalDate.now()).getYears();
//        return age >= minAge && age <= maxAge;
//    }

    public List<UserBasicInfo> findTeachers() {
        return userRepository.findTeachers();
    }

    public List<EmployeeProfile> findEmployees() {
        return userRepository.findEmployees();
    }

    public List<UserBasicInfo> getChildrenSimpleData(UUID id) {
        List<UserBasicInfo> childrenSimpleList = userRepository.findChildrenBasicInfo(id);
        return childrenSimpleList;
    }

    public EmployeeProfile getEmployeeById(UUID id) {
        User employee = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("Employee with id %s does not exist.", id)
                ));
        return new EmployeeProfile(id, employee.getFirstName(), employee.getLastName(),
                employee.getHeadshot(), employee.getPosition(), employee.getDescription());
    }

    public void deleteEmployee(UUID employeeId) { // todo: use deleteChild() method?
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
        return originalEmployee;
    }
}

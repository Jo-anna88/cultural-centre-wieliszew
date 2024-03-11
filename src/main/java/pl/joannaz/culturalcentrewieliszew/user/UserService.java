package pl.joannaz.culturalcentrewieliszew.user;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.joannaz.culturalcentrewieliszew.course.Course;
import pl.joannaz.culturalcentrewieliszew.course.CourseDetails;
import pl.joannaz.culturalcentrewieliszew.course.CourseDetailsRepository;
import pl.joannaz.culturalcentrewieliszew.course.CourseRepository;
import pl.joannaz.culturalcentrewieliszew.linkEntities.UserCourse;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

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
    public User addChild(UUID parentId, User childUser) {
        // Retrieve the parent user
        if (!userRepository.existsById(parentId)) {
            throw new RuntimeException("Parent user not found");
        }
        // Set the parent ID for the child user
        childUser.setParentId(parentId);
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
        return userRepository.save(originalChild);
    }

    public User getChild (UUID childId) {
        return userRepository.findById(childId).get(); // todo: check isPresent()
    }

    public void deleteChild (UUID childId) {
        userRepository.deleteById(childId); // todo: courses list should be deleted too
    }

    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    }

    public List<Course> getCoursesForUser(UUID userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<UserCourse> userCourses = user.getCourses();
            return userCourses.stream()
                    .map(UserCourse::getCourse)
                    .collect(Collectors.toList());
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
        }
        else {
            throw new RuntimeException("Sorry, there is no more available slots for this course.");
        }
    }

//    public boolean isAgeCorrect(LocalDate dob, int minAge, int maxAge) {
//        int age = Period.between(dob, LocalDate.now()).getYears();
//        return age >= minAge && age <= maxAge;
//    }

    public List<UserDetailsDTO> findTeachers() {
        return userRepository.findTeachers();
    }
}

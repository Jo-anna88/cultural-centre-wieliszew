package pl.joannaz.culturalcentrewieliszew.course;


import jakarta.persistence.Transient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.joannaz.culturalcentrewieliszew.linkEntities.UserCourse;

import java.util.*;
import java.util.stream.Collectors;

import static pl.joannaz.culturalcentrewieliszew.utils.constants.SIMPLE_TEXT;

@Service
//@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseDetailsRepository detailsRepository;

    //@Autowired
    public CourseService (CourseRepository courseRepository, CourseDetailsRepository detailsRepository) {
        this.courseRepository = courseRepository;
        this.detailsRepository = detailsRepository;
    }

    public List<Course> getAllCourses() {
        //log.info("Fetching all courses.");
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) { // UUID
        //return new Course("assets/icons/ballet-shoes.png", "Ballet", "Anna Baletowicz", SIMPLE_TEXT);
        return courseRepository.findById(id).get(); // todo: check isPresent() (not return null!)
    }

    public CourseDetails getDetailsById (Long id) {
        boolean detailsExists = detailsRepository.existsById(id);
        // throw new NoSuchElementException("");
        return detailsExists ? detailsRepository.findById(id).get() : null; // todo: check isPresent() (not return null!)
    }

    public Course addCourse(Course course) {
        //log.info("Saving new course: {} to the database", course.getName());
        return courseRepository.save(course);
    }

    public void addAllCourses(List<Course> courses) {
        courseRepository.saveAll(courses);
    }

    // @Transient - when we have setters in our class
    // thanks to that, we don't have to implement any JPQL query
    public Course updateCourse(Course updatedCourse) {
        Course originalCourse = courseRepository.findById(updatedCourse.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Course with this id does not exist."
        ));
        if(!Objects.equals(originalCourse.getDescription(), updatedCourse.getDescription())) {
            originalCourse.setDescription(updatedCourse.getDescription());
        }
        if(!Objects.equals(originalCourse.getName(), updatedCourse.getName())) {
            Optional<Course> courseOptional = courseRepository.findCourseByName(updatedCourse.getName());
            if (courseOptional.isPresent()) {
                throw new IllegalStateException("Such a name already exists.");
            }
            originalCourse.setName(updatedCourse.getName());
        }
        // if...
        originalCourse.setImgSource(updatedCourse.getImgSource());
        // if...
        originalCourse.setTeacher(updatedCourse.getTeacher());
        // if...
        originalCourse.setCategory(updatedCourse.getCategory());
        return courseRepository.save(originalCourse);
        //return originalCourse; //because we use @Transactional we can use setters against methods from repository
        //return new Course(courseRepository.save(originalCourse));
    }

    public Long deleteCourse(Long id) { // UUID
        boolean exists = courseRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Such a course does not exist.");
        }

        boolean detailsExist = detailsRepository.existsById(id);
        if (detailsExist) {
            detailsRepository.deleteById(id);
        }

        courseRepository.deleteById(id);
        return id;
    }

    public CourseDetails addCourseDetails(CourseDetails courseDetails) {
        Course course = courseRepository.findById(courseDetails.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Course Details with this id does not exist."
                ));
        courseDetails.setCourse(course);
        return detailsRepository.save(courseDetails);
    }

    public CourseDetails updateCourseDetails(CourseDetails updatedCourseDetails) {
        CourseDetails originalCourseDetails = detailsRepository.findById(updatedCourseDetails.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Course Details with this id does not exist."
                ));
//        originalCourseDetails.setMinAge(updatedCourseDetails.getMinAge());
//        originalCourseDetails.setMaxAge(updatedCourseDetails.getMaxAge());
//        originalCourseDetails.setPrice(updatedCourseDetails.getPrice());
//        originalCourseDetails.setMaxParticipantsNumber(updatedCourseDetails.getMaxParticipantsNumber());
//        originalCourseDetails.setLessonDurationMinutes(updatedCourseDetails.getLessonDurationMinutes());
//        originalCourseDetails.setDate(updatedCourseDetails.getDate());
//        originalCourseDetails.setRoomId(updatedCourseDetails.getRoomId());
        originalCourseDetails.update(updatedCourseDetails);
        return detailsRepository.save(originalCourseDetails);
    }

    public Long deleteCourseDetails(Long id) {
        boolean detailsExist = detailsRepository.existsById(id);
        if (!detailsExist) {
            throw new IllegalStateException("Course details do not exist.");
        }
        detailsRepository.deleteById(id);
        return id;
    }

    public List<String> getParticipantsByCourseId(Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            List<UserCourse> userCourses = course.getParticipants();
            return userCourses.stream()
                    .map(UserCourse::getParticipant)
                    .map(user -> user.getFirstName() + " " + user.getLastName()) // Map User to UserDTO using constructor reference
                    .collect(Collectors.toList());
        } else {
            // todo: Handle the case where user with the given ID is not found
            return Collections.emptyList();
        }
    }
}

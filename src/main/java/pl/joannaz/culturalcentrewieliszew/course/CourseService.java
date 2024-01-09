package pl.joannaz.culturalcentrewieliszew.course;


import jakarta.persistence.Transient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static pl.joannaz.culturalcentrewieliszew.utils.constants.SIMPLE_TEXT;

@Service
//@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;

    //@Autowired
    public CourseService (CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        //log.info("Fetching all courses.");
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) { // UUID
        //return new Course("assets/icons/ballet-shoes.png", "Ballet", "Anna Baletowicz", SIMPLE_TEXT);
        return courseRepository.findById(id).get();
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
        return courseRepository.save(originalCourse);
        //return originalCourse; //because we use @Transactional we can use setters against methods from repository
        //return new Course(courseRepository.save(originalCourse));
    }

    public Long deleteCourse(Long id) { // UUID
        boolean exists = courseRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Such a course does not exist.");
        }
        courseRepository.deleteById(id);
        return id;
    }
}

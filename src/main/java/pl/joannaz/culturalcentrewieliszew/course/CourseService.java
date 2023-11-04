package pl.joannaz.culturalcentrewieliszew.course;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static pl.joannaz.culturalcentrewieliszew.utils.constants.SIMPLE_TEXT;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    //@Autowired
    public CourseService (CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return List.of(
                new Course("assets/icons/ballet-shoes.png", "Ballet", "Anna Baletowicz", SIMPLE_TEXT),
                new Course("assets/icons/chess.png", "Chess", "Igor Szachista", SIMPLE_TEXT),
                new Course("assets/icons/guitar.png", "Guitar", "Jan Muzyk", SIMPLE_TEXT),
                new Course("assets/icons/pottery.png", "Pottery", "Katarzyna Waza", SIMPLE_TEXT),
                new Course("assets/icons/theatre.png", "Theatre", "Agnieszka Teatralna", SIMPLE_TEXT),
                new Course("assets/icons/microphone.png", "Vocal", "Łukasz Wokalista", SIMPLE_TEXT)
        );
        //return courseRepository.findAll();
    }

    public Course getCourseById(UUID id) {
        return new Course("assets/icons/ballet-shoes.png", "Ballet", "Anna Baletowicz", SIMPLE_TEXT);
        //return courseRepository.findById(id);
    }

    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course updateCourse(Course updatedCourse) {
        Course originalCourse = courseRepository.findById(updatedCourse.getId()).get();
        originalCourse.setDescription(updatedCourse.getDescription());
        originalCourse.setName(updatedCourse.getName());
        originalCourse.setImgSource(updatedCourse.getImgSource());
        originalCourse.setTeacher(updatedCourse.getTeacher());
        return new Course(courseRepository.save(originalCourse));
    }

    public int deleteCourse(UUID id) {
        courseRepository.deleteById(id);
        return 1;
    }
}

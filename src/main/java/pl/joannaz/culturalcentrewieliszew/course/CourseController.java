package pl.joannaz.culturalcentrewieliszew.course;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/classes")
public class CourseController {

    private final CourseRepository courseRepository;
    private final CourseService courseService;

    //@Autowired
    public CourseController(CourseService courseService, CourseRepository courseRepository) {
        this.courseService = courseService;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public List<Course> getAllCourses(HttpServletResponse response) throws InterruptedException {
         //Thread.sleep(3000); // to check dealing with slow REST responses
         return courseService.getAllCourses();
        //response.setStatus(500);
        //return null;
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable("id") UUID id)
    {
        return courseService.getCourseById(id);
        //return courseRepository.findById(id).get();
    }

    @PostMapping()
    public Course addCourse(@RequestBody Course course) {
        return courseService.addCourse(course);
    }

    @PutMapping()
    public Course updateCourse(@RequestBody Course updatedCourse) {
        return courseService.updateCourse(updatedCourse);
    }

    @DeleteMapping("/{id}")
    public UUID deleteCourse(@PathVariable("id") UUID id) {
        System.out.println("deleteCourse: " + id);
        return courseService.deleteCourse(id);
    }
}

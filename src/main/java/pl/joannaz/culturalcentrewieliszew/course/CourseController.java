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
@RequestMapping(path="/api/classes")
public class CourseController {

    private final CourseService courseService;

    //@Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getAllCourses(HttpServletResponse response) throws InterruptedException {
         //Thread.sleep(3000); // to check dealing with slow REST responses
        return courseService.getAllCourses();
        //response.setStatus(500);
        //return null;
    }

    /*
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok().body(courseService.getAllCourses());
    }
     */

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable("id") Long id) { // UUID id
        return courseService.getCourseById(id);
        //return courseRepository.findById(id).get();
    }

    @GetMapping("/{id}/details")
    public CourseDetails getDetailsById(@PathVariable("id") Long id) {
        return courseService.getDetailsById(id);
    }

    @PostMapping()
    public Course addCourse(@RequestBody Course course) {
        return courseService.addCourse(course); // id will be automatically added
    }

    @PostMapping("/{id}/details")
    public CourseDetails addCourseDetails(@RequestBody CourseDetails courseDetails) {
        return courseService.addCourseDetails(courseDetails);
    }

    @PutMapping()
    public Course updateCourse(@RequestBody Course updatedCourse) {
        return courseService.updateCourse(updatedCourse);
    }

    @PutMapping("/{id}/details")
    public CourseDetails updateCourseDetails(@RequestBody CourseDetails updatedCourseDetails) {
        return courseService.updateCourseDetails(updatedCourseDetails);
    }

    @DeleteMapping("/{id}")
    public Long deleteCourse(@PathVariable("id") Long id) { // UUID
        System.out.println("deleteCourse: " + id);
        return courseService.deleteCourse(id);
    }

    @DeleteMapping("/{id}/details")
    public Long deleteCourseDetails(@PathVariable("id") Long id) { // UUID
        System.out.println("deleteCourseDetails: " + id);
        return courseService.deleteCourseDetails(id);
    }
}

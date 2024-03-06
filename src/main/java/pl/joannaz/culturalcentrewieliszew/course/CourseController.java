package pl.joannaz.culturalcentrewieliszew.course;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path="/api/classes")
public class CourseController {

    private final CourseService courseService;

    //@Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<CourseDTO> getAllCourses(HttpServletResponse response) throws InterruptedException {
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
    public CourseDTO getCourseById(@PathVariable("id") Long id) { // UUID id
        return courseService.getCourseById(id);
    }

    @GetMapping("/{id}/details")
    public CourseDetails getDetailsById(@PathVariable("id") Long id) {
        return courseService.getDetailsById(id);
    }

    @PostMapping()
    public CourseDTO addCourse(@RequestBody CourseDTO courseDTO) {
        return courseService.addCourse(courseDTO); // id will be automatically added
    }

    @PostMapping("/{id}/details")
    public CourseDetails addCourseDetails(@RequestBody CourseDetails courseDetails) {
        return courseService.addCourseDetails(courseDetails);
    }

    @PutMapping()
    public CourseDTO updateCourse(@RequestBody CourseDTO updatedCourseDTO) {
        return courseService.updateCourse(updatedCourseDTO);
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

    @GetMapping("/{id}/participants")
    public List<String> getParticipantsByCourseId(@PathVariable("id") Long id) {
        return courseService.getParticipantsByCourseId(id);
    }

    @GetMapping("/search")
    public List<CourseDTO> searchCourses(
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(required = false) String teacher,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location) {
        return courseService.findCoursesByCriteria(minAge, maxAge, price, teacher, category, name, location);
    }

}

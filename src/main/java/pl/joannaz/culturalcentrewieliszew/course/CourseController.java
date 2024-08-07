package pl.joannaz.culturalcentrewieliszew.course;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import pl.joannaz.culturalcentrewieliszew.user.User;
import pl.joannaz.culturalcentrewieliszew.user.UserService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/classes")
public class CourseController {

    private final CourseService courseService;

    //@Autowired
    public CourseController(CourseService courseService) { this.courseService = courseService; }

    @GetMapping
    public List<CourseDTO> getAllCourses() throws InterruptedException {
        // Thread.sleep(3000); // to check dealing with slow REST responses
         return courseService.getAllCourses();
        // throw new RuntimeException("test message"); // to check dealing with http error responses
    }

    @GetMapping("/{id}")
    public CourseDTO getCourseById(@PathVariable("id") Long id) { // UUID id
        return courseService.getCourseById(id);
    }

    @GetMapping("/{id}/details")
    public CourseDetailsDTO getDetailsById(@PathVariable("id") Long id) {
        return courseService.getDetailsById(id);
    }

    @PostMapping()
    public CourseDTO addCourse(@RequestBody CourseDTO courseDTO) {
        return courseService.addCourse(courseDTO); // id will be automatically added
    }

    @PostMapping("/{id}/details")
    public CourseDetailsDTO addCourseDetails(@RequestBody CourseDetailsDTO courseDetailsDTO) {
        return courseService.addCourseDetails(courseDetailsDTO);
    }

    @PutMapping()
    public CourseDTO updateCourse(@RequestBody CourseDTO updatedCourseDTO) {
        return courseService.updateCourse(updatedCourseDTO);
    }

    @PutMapping("/{id}/details")
    public CourseDetailsDTO updateCourseDetails(@RequestBody CourseDetailsDTO updatedCourseDetailsDTO) {
        return courseService.updateCourseDetails(updatedCourseDetailsDTO);
    }

    @DeleteMapping("/{id}")
    public Long deleteCourse(@PathVariable("id") Long id) { // UUID
        return courseService.deleteCourse(id);
    }

    @DeleteMapping("/{id}/details")
    public Long deleteCourseDetails(@PathVariable("id") Long id) { // UUID
        System.out.println("deleteCourseDetails: " + id);
        return courseService.deleteCourseDetails(id);
    }

//    @GetMapping("/{id}/participants")
//    public List<String> getParticipantsByCourseId(@PathVariable("id") Long id) {
//        return courseService.getParticipantsByCourseId(id);
//    }

    @GetMapping("/search")
    public List<CourseDTO> searchCourses(HttpServletResponse response,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(required = false) UUID teacher,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer location) {
        //response.setStatus(500);
        //return null;
        return courseService.findCoursesByCriteria(minAge, maxAge, price, teacher, category, name, location);
    }

    @GetMapping("/led-by/{teacherId}")
    public List<CourseBasicInfo> getCoursesLedByTeacher(@PathVariable UUID teacherId) {
        return courseService.getCoursesLedByTeacher(teacherId);
    }
}

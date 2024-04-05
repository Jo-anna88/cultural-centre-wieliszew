package pl.joannaz.culturalcentrewieliszew.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.joannaz.culturalcentrewieliszew.course.CourseDTO;

import java.util.*;

@RestController
@RequestMapping(path="/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }
//    @GetMapping
//    public List<User> getUsers() {
//        return List.of(new User(UUID.randomUUID(), "name", "aaa@o2.pl"));
//    }
    /*
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().parallelStream().map(user -> new UserDTO(user)).collect(Collectors.toList());
    }
     */
    /*
    ***save a new user***
    * Use a strong hashing algorithm like BCrypt *
    String encodedPassword = passwordEncoder.encode(rawPassword);
    user.setPassword(encodedPassword);
    userRepository.save(user);
     */
    @GetMapping("/profile")
    public UserDTO getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the authentication object is not null and is an instance of UserDetails
        //if(!(authentication instanceof AnonymousAuthenticationToken))
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            // Cast the principal to User
            return new UserDTO((User) authentication.getPrincipal());
        }
        throw new RuntimeException("cannot get current user's profile");
    }

    @GetMapping("/full-name") // user's full name // todo: full-name (or UserDetailsDTO) could be returned as a result of login
    public Map<String,String> getFullName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser;
        // Check if the authentication object is not null and is an instance of UserDetails
        //if(!(authentication instanceof AnonymousAuthenticationToken))
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            // Cast the principal to User
            currentUser = (User) authentication.getPrincipal();
            Map<String,String> response = new HashMap<>(2);
            response.put("firstName", currentUser.getFirstName());
            response.put("lastName", currentUser.getLastName());
            return response;
        }
        throw new RuntimeException("cannot get current user's name and surname");
    }

    @GetMapping("/user-simple")
    public UserBasicInfo getUserSimpleData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            User currentUser = (User) authentication.getPrincipal();
            return new UserBasicInfo(currentUser.getId(), currentUser.getFirstName(), currentUser.getLastName());
        } else {
            throw new RuntimeException("Cannot get children data for this user.");
        }
    }

    // User's children endpoints:
    @GetMapping("/children-simple")
    public List<UserBasicInfo> getChildrenSimpleData(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            UUID id = ((User) authentication.getPrincipal()).getId();
            return userService.getChildrenSimpleData(id);
        } else {
            throw new RuntimeException("Cannot get children data for this user.");
        }
    }

    @GetMapping("/children")
    public List<UserDTO> getChildrenByParentId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UUID parentId = ((User) authentication.getPrincipal()).getId();
            List<User> children = userService.getChildren(parentId);
            List<UserDTO> childrenDTO = new ArrayList<>(children.size());
            for (User child : children) {
                childrenDTO.add(new UserDTO(child));
            }
            return childrenDTO;
        }
        throw new RuntimeException("cannot get list of children");
    }
    @PostMapping("/child")
    public UserDTO addChild(@RequestBody User child) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            User parentUser = (User) authentication.getPrincipal();
            User newChild = userService.addChild(parentUser, child);
            return new UserDTO(newChild);
        }
        throw new RuntimeException("cannot add a child");
    }

    @PutMapping("/child")
    public UserDTO updateChild(@RequestBody User child) {
        return new UserDTO(userService.updateChild(child));
    }

    @DeleteMapping("/child/{id}")
    public void deleteChild(@PathVariable("id") UUID id) {
        userService.deleteChild(id);
    }

    @GetMapping("child/{id}")
    public UserDTO getChild(@PathVariable("id") UUID id) {
        return new UserDTO(userService.getUser(id));
    }

    // User's courses endpoints:

    @GetMapping("/courses")
    public List<CourseDTO> getCourses() {
        User currentUser = userService.getCurrentUser();
        return userService.getCoursesForUser(currentUser.getId());
    }

    @GetMapping("/courses/{id}")
    public List<CourseDTO> getCoursesById(@PathVariable("id") UUID id) {
        return userService.getCoursesForUser(id);
    }

    @GetMapping("/join-course/{courseId}")
    public void joinCourse(@PathVariable("courseId") Long courseId) {
        try {
            userService.addCourse(courseId);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("You are already enrolled for this class.");
        }
    }

    @GetMapping("/join-course/{courseId}/{userId}")
    public void joinCourseByUserId(@PathVariable("courseId") Long courseId, @PathVariable("userId") UUID userId) {
        userService.joinCourseByUserId(courseId, userId);
    }

    // Teacher endpoint:

    @GetMapping("/teachers")
    public List<UserBasicInfo> getTeachers() {
        return userService.findTeachers();
    }

    // Employee endpoints:

    @GetMapping("/employees")
    public List<EmployeeProfile> getEmployees() {
        return userService.findEmployees();
    }

    @GetMapping("/employee/{id}")
    public EmployeeProfile getEmployeeById(@PathVariable("id") UUID id) {
        return userService.getEmployeeById(id);
    }

    @PostMapping("/employee")
    public UserDTO addEmployee(@RequestBody UserDTO employee) {
        return new UserDTO(userService.addEmployee(employee));
    }

    @PutMapping("/employee")
    public UserDTO updateEmployee(@RequestBody UserDTO employee) {
        return new UserDTO(userService.updateEmployee(employee));
    }

    @DeleteMapping("/employee/{id}")
    public void deleteEmployee(@PathVariable("id") UUID id) {
        userService.deleteEmployee(id);
    }
}

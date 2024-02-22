package pl.joannaz.culturalcentrewieliszew.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/{id}/children")
    public List<UserDTO> getChildrenByParentId(@PathVariable("id") UUID id) {
        List<User> children = userService.getChildren(id);
        List<UserDTO> childrenDTO = new ArrayList<>(children.size());
        for (User child : children) {
            childrenDTO.add(new UserDTO(child));
        }
        return childrenDTO;
    }
    @PostMapping("/{id}")
    public UserDTO addChild(@PathVariable UUID id, @RequestBody User child) {
        User newChild = userService.addChild(id, child); // id will be automatically added
        return new UserDTO(newChild);
    }

    @GetMapping("/profile")
    public UserDTO getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the authentication object is not null and is an instance of UserDetails
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            // Cast the principal to User
            return new UserDTO((User) authentication.getPrincipal());
        }
        throw new RuntimeException("cannot get current user's profile");
    }

    @GetMapping("/userBasicData") // user name and surname
    public String getUserNameAndSurname() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser;
        // Check if the authentication object is not null and is an instance of UserDetails
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            // Cast the principal to User
            currentUser = (User) authentication.getPrincipal();
            String userNameAndSurname = "{\"firstName\":\"" + currentUser.getFirstName() + "\",\"lastName\":\"" + currentUser.getLastName() + "\"}";
            return userNameAndSurname;
        }
        throw new RuntimeException("cannot get current user's name and surname");
    }
}

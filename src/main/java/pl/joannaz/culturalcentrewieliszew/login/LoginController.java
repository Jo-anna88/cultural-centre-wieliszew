package pl.joannaz.culturalcentrewieliszew.login;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.joannaz.culturalcentrewieliszew.user.User;
import pl.joannaz.culturalcentrewieliszew.user.UserDTO;
import pl.joannaz.culturalcentrewieliszew.user.UserService;

import java.security.interfaces.RSAPrivateKey;
import java.util.*;

@RestController
@RequestMapping(path = "/api/auth")
public class LoginController {
    private int PRIVATE_KEY = 123;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public LoginController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(path="/login")
    public UserDTO login (@RequestBody Map<String,String> credentials) {
        //credentials - LinkedHashMap, keys: username and password
        Optional<User> user = userService.findUserByUsername(credentials.get("username"));
        if (user.isPresent()) {
            if (passwordEncoder.matches(credentials.get("password"), user.get().getPassword())) {
                System.out.println("Password is correct");
                return new UserDTO(user.get());
                // Passwords match, proceed with authentication
                // Create an Authentication object, set it in the SecurityContext, etc.
            } else {
                System.out.println("Password is incorrect");
                return null;
                // Error: Passwords do not match, handle unsuccessful login
            }
        }
        // Error: User not found, handle unsuccessful login.
        System.out.println("User not found. Please, try again with other username.");
        return null;
        //return credentials;
    }

    @PostMapping(path="/signup") // id=null
    public UserDTO signup (@RequestBody User newUser) {
        newUser.setId(UUID.randomUUID());
        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);
        userService.addUser(newUser);
        return new UserDTO(newUser);
    }
}

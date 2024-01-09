package pl.joannaz.culturalcentrewieliszew.login;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.joannaz.culturalcentrewieliszew.user.User;
import pl.joannaz.culturalcentrewieliszew.user.UserService;

import java.security.interfaces.RSAPrivateKey;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

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
    public Map<String,String> login (@RequestBody Map<String,String> credentials) {
        System.out.println(credentials);
        return credentials;
    }

    @PostMapping(path="/signup") // id=null
    public User signup (@RequestBody User newUser) {
        newUser.setId(UUID.randomUUID());
        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);
        userService.addUser(newUser);
        System.out.println(newUser);
        return newUser;
    }
}

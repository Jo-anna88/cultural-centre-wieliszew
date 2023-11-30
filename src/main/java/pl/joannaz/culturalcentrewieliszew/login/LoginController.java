package pl.joannaz.culturalcentrewieliszew.login;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.joannaz.culturalcentrewieliszew.user.User;

import java.security.interfaces.RSAPrivateKey;

@RestController
@RequestMapping(path = "/api/auth")
public class LoginController {
    private int PRIVATE_KEY = 123;

    @PostMapping(path="/login")
    public Object login (@RequestBody Object usernameAndPswd) {
        System.out.println(usernameAndPswd);
        return usernameAndPswd;
    }

    @PostMapping(path="/signup")
    public Object signup (@RequestBody Object newUser) {
        System.out.println(newUser);
        return newUser;
    }
}

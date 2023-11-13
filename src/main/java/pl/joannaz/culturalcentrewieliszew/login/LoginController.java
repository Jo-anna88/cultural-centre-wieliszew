package pl.joannaz.culturalcentrewieliszew.login;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.interfaces.RSAPrivateKey;

@RestController
@RequestMapping(path="/login")
public class LoginController {
    private int PRIVATE_KEY = 123;

    @PostMapping
    public Object login (@RequestBody Object usernameAndPswd) {
        System.out.println(usernameAndPswd);
        return usernameAndPswd;
    }
}

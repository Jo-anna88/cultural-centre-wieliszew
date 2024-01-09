package pl.joannaz.culturalcentrewieliszew.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/user")
public class UserController {
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
}

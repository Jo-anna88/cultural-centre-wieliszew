package pl.joannaz.culturalcentrewieliszew.user;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService { //interface UserService ?
    private final UserRepository userRepository;

    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //private final User user;
    //User saveUser(User user); ?
    public User addUser(User user) {
        return userRepository.save(user);
    }
    public Optional<User> findUserByUsername(String username) {return userRepository.findByUsername(username);}

}

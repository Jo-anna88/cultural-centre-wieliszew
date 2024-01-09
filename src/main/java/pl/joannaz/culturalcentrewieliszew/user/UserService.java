package pl.joannaz.culturalcentrewieliszew.user;

import org.springframework.stereotype.Service;

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

}

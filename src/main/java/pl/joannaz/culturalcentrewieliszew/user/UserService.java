package pl.joannaz.culturalcentrewieliszew.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService { //interface UserService ?
    private final UserRepository userRepository;

    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //private final User user;
    //User saveUser(User user); ?
    public User addUser(User user) {
        return this.userRepository.save(user);
    }
    public Optional<User> findUserByUsername(String username) {return this.userRepository.findByUsername(username);}
    public boolean existsByUsername(String username) {return this.userRepository.existsByUsername(username);}
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found in the database."));
    }
}

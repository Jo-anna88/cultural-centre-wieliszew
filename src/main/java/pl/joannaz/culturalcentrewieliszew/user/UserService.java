package pl.joannaz.culturalcentrewieliszew.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public List<User> getChildren(UUID userId) {
        return userRepository.findByParentId(userId);
    }
    public User addChild(UUID parentId, User childUser) {
        // Retrieve the parent user
        if (!userRepository.existsById(parentId)) {
            throw new RuntimeException("Parent user not found");
        }
        // Set the parent ID for the child user
        childUser.setParentId(parentId);
        // Save the child user
        return userRepository.save(childUser);
    }
}

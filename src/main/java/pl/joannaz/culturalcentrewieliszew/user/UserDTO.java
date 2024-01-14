package pl.joannaz.culturalcentrewieliszew.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.springframework.context.annotation.Bean;

import java.io.Serializable;
import java.util.UUID;
@Data
public class UserDTO {
    // without pswd
    // constructor - create UserDTO from User
    // convertToUser (asUser())
    private UUID id;
    private String firstName;
    private String lastName;
    private String phone;
    private String username; // e.g. email, login
    private Role role;
    public UserDTO (User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
        this.username = user.getUsername();
        this.role = user.getRole();
    }
}

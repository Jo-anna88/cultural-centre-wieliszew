package pl.joannaz.culturalcentrewieliszew.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.springframework.context.annotation.Bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;
@Data
public class UserDTO implements Serializable {
    // without pswd
    // constructor - create UserDTO from User
    // convertToUser (asUser())
    private UUID id;
    private String firstName;
    private String lastName;
    private String phone;
    private String username; // e.g. email, login
    private LocalDate dob; // needed during signup
    private int age;
    private String headshot;
    private Role role;
    private String position; // for Employee/Admin
    private String description; // for Employee/Admin

    public UserDTO (User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
        this.username = user.getUsername();
        this.dob = user.getDob();
        this.age = Period.between(user.getDob(), LocalDate.now()).getYears();
        this.headshot = user.getHeadshot();
        this.role = user.getRole();
        this.position = user.getPosition(); // ?
        this.description = user.getDescription(); // ?
    }
}

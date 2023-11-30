package pl.joannaz.culturalcentrewieliszew.user;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data // for setters and getters
@Builder // to use Builder design pattern
@NoArgsConstructor
@AllArgsConstructor // needed for Builder
@Table(name="_user")
@Entity(name="User")
public class User {//implements UserDetails { // model (class called 'User' exists in PostgreSQL, so we should use other name
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;
    private String firstName;
    private String lastName;
    private String phone;
    private String username; // e.g. email, login
    private String password;
    @Enumerated(EnumType.STRING) // it tells Spring Boot that it is enum
    private Role role;
    //private String token;
/*
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    // it is done by Lombok, so maybe it is not needed
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

 */
}

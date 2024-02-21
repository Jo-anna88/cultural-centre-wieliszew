package pl.joannaz.culturalcentrewieliszew.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
@Table(
        name="users",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "username")
        })
@Entity(name="User")
public class User implements UserDetails
{ // model (class called 'User' exists in PostgreSQL, so we should use other name
    // it is important that SpringSecurity has its own class/interface called User too!
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;
    private UUID parentId; // for relation with subaccounts
    private String firstName;
    private String lastName;
    private String phone;
    private String username; // e.g. email, login
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role; // in this app user can have only one role
    // or:
    // @ManyToMany(fetch = FetchType.EAGER)
    // Collection<Role> roles = new ArrayList<>(); // 'eager' because we want to load all the roles whenever we load the user
    // or:
    // @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    // private Set<Authority> roles = new HashSet<>();
    // or: //bezkoder//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "user_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id"))
//    private Set<Role> roles = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
    /*
    for many roles: //bezkoder//
        List<GrantedAuthority> authorities = user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
        .collect(Collectors.toList());
     */

    @Override
    // it is done by Lombok, so maybe it is not needed
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

}

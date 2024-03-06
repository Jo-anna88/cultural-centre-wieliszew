package pl.joannaz.culturalcentrewieliszew.user;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.joannaz.culturalcentrewieliszew.course.Course;
//import pl.joannaz.culturalcentrewieliszew.course.CourseDetails;
//import pl.joannaz.culturalcentrewieliszew.culturalEvent.CulturalEvent;
import pl.joannaz.culturalcentrewieliszew.linkEntities.UserCourse;
//import pl.joannaz.culturalcentrewieliszew.linkEntities.UserCulturalEvent;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

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
//@JsonIdentityInfo( // for Bi-directional @OneToOne association (to remove recursion in JSON mapper)
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id")
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

    //@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    //@JoinTable(name = "user_course",
    //        joinColumns = { @JoinColumn(name = "participant_id") }, // or user_id
    //        inverseJoinColumns = { @JoinColumn(name = "course_id") })
    //private Set<Course> courses = new HashSet<>();
    @OneToMany(
            mappedBy = "participant",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    //@JsonIgnoreProperties("participant")
    @JsonManagedReference(value="user-course")
    private List<UserCourse> courses = new ArrayList<>();

    //@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    //private Set<CulturalEvent> culturalEvents = new HashSet<>();
//    @OneToMany(
//            mappedBy = "participant",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true,
//            fetch = FetchType.EAGER
//    )
//    private List<UserCulturalEvent> culturalEvents = new ArrayList<>();

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

    // helper/utility methods for @OneToMany associations
    public void addCourse(Course course) {
        UserCourse userCourse = new UserCourse(this, course);
        courses.add(userCourse);
        course.getParticipants().add(userCourse);
    }

    public void removeCourse(Course course) {
        UserCourse userCourse = new UserCourse(this, course);
        course.getParticipants().remove(userCourse);
        courses.remove(userCourse);
        userCourse.setParticipant(null);
        userCourse.setCourse(null);
    }

//    public void addCulturalEvent(CulturalEvent culturalEvent) {
//        UserCulturalEvent userCulturalEvent = new UserCulturalEvent(this, culturalEvent);
//        culturalEvents.add(userCulturalEvent);
//        culturalEvent.getParticipants().add(userCulturalEvent);
//    }
//
//    public void removeCulturalEvent(CulturalEvent culturalEvent) {
//        UserCulturalEvent userCulturalEvent = new UserCulturalEvent(this, culturalEvent);
//        culturalEvent.getParticipants().remove(userCulturalEvent);
//        culturalEvents.remove(userCulturalEvent);
//        userCulturalEvent.setParticipant(null);
//        userCulturalEvent.setCulturalEvent(null);
//    }

    /*
    // helper/utility methods for @ManyToMany associations
    public void addCourse(Course course) {
        courses.add(course);
        courses.getUsers().add(this);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        courses.getUsers().remove(this);
    }

    public void addCulturalEvent(CulturalEvent culturalEvent) {
        culturalEvents.add(culturalEvent);
        culturalEvents.getUsers().add(this);
    }

    public void removeCulturalEvent(CulturalEvent culturalEvent) {
        culturalEvents.remove(culturalEvent);
        culturalEvents.getUsers().remove(this);
    }
    */
}

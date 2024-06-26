package pl.joannaz.culturalcentrewieliszew.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.joannaz.culturalcentrewieliszew.course.Course;
import pl.joannaz.culturalcentrewieliszew.courseregistration.CourseRegistration;
import pl.joannaz.culturalcentrewieliszew.culturalevent.CulturalEvent;
import pl.joannaz.culturalcentrewieliszew.culturaleventbooking.CulturalEventBooking;

import java.time.LocalDate;
import java.util.*;

import static pl.joannaz.culturalcentrewieliszew.utils.constants.PASSWORD;

@Data // for setters and getters
@NoArgsConstructor
@Table(
        name="users"
//        ,uniqueConstraints = {
//            @UniqueConstraint(columnNames = "username")}
        )
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

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private User parent; // for relation with subaccounts


    @Column(
            name="first_name",
            nullable = false
    )
    private String firstName;

    @Column(
            name="last_name",
            nullable = false
    )
    private String lastName;

    @Column(
            name="phone",
            nullable = false
    )
    private String phone;

    @Column(
            name="username",
            nullable = false,
            unique = true
    )
    private String username; // e.g. email, login

    @Column(
            name="password"
    )
    private String password;

    @Column(
            name="dob",
            nullable = false
    )
    private LocalDate dob; // date-of-birth

    @Column(
            name="headshot",
            nullable = false
    )
    private String headshot; // headshot photo source (e.g. "assets/images/avatar1.svg")

    @Column(
            name="role",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private Role role; // in this app user can have only one role

    @Column(
            name="position"
    )
    private String position;

    @Column(
            name="description",
            columnDefinition = "TEXT"
    )
    private String description; // employee description
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
            orphanRemoval = true
    )
    @JsonManagedReference(value="user-course")
    private List<CourseRegistration> courses = new ArrayList<>(); // for CLIENT

    /*
    @OneToMany(
            mappedBy = "participant",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference(value="user-event")
    private List<CulturalEventBooking> culturalEvents = new ArrayList<>(); // for CLIENT
     */

    // for development:
    // constructor for Client
    public User(String firstName, String lastName, String phone, String username, String dob, String headshot) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.username = username;
        this.password = PASSWORD; // test
        this.dob = LocalDate.parse(dob); // e.g. "2020-10-30"
        this.headshot = headshot;
        this.role = Role.CLIENT;
        //this.position = null;
        //this.description = null;
    }

    // for development:
    // constructor for Client's child
    public User(User parent, String firstName, String lastName, String phone, String username, String dob, String headshot) {
        this.parent = parent;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone; //parent's phone
        this.username = username; //parent's username + "/firstName" + "/lastName"
        // this.password = null
        this.dob = LocalDate.parse(dob);
        this.headshot = headshot;
        this.role = Role.CLIENT;
        // this.position = null
        // this.description = null
    }

    // constructor for Employee or Admin
    public User(String firstName, String lastName, String phone, String dob,
                Role role, String position, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.username = UserHelper.createEmployeeUsername(firstName, lastName);
        this.password = PASSWORD;
        this.dob = LocalDate.parse(dob);
        this.headshot = UserHelper.isFemale(firstName) ? "assets/images/avatar2.svg" : "assets/images/avatar1.svg";
        this.role = role;
        this.position = position;
        this.description = description;
    }

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
        CourseRegistration courseRegistration = new CourseRegistration(this, course);
        courses.add(courseRegistration);
        course.getParticipants().add(courseRegistration);
    }

    public void removeCourse(Course course) {
        List<CourseRegistration> ucList = course.getParticipants();
        CourseRegistration record = ucList.stream().filter(uc -> uc.getParticipant().getId().equals(this.id)).findFirst().get();
        course.getParticipants().remove(record);
        courses.remove(record);
        record.setParticipant(null);
        record.setCourse(null);
    }
//
//    public void addCulturalEvent(CulturalEvent culturalEvent) {
//        CulturalEventBooking culturalEventBooking = new CulturalEventBooking(this, culturalEvent);
//        culturalEvents.add(culturalEventBooking);
//        culturalEvent.getParticipants().add(culturalEventBooking);
//    }
//
//    public void removeCulturalEvent(CulturalEvent culturalEvent) {
//        List<CulturalEventBooking> uceList = culturalEvent.getParticipants();
//        CulturalEventBooking record = uceList.stream().filter(uce -> uce.getParticipant().getId().equals(this.id)).findFirst().get();
//        culturalEvent.getParticipants().remove(record);
//        culturalEvents.remove(record);
//        record.setParticipant(null);
//        record.setCulturalEvent(null);
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

package pl.joannaz.culturalcentrewieliszew.courseregistration;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import pl.joannaz.culturalcentrewieliszew.course.Course;
import pl.joannaz.culturalcentrewieliszew.user.User;
@Table(name = "course_registration")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="CourseRegistration") // join table between a user and a course
public class CourseRegistration {
    @Id
    @ManyToOne
    @JsonBackReference(value="user-course")
    private User participant;

    @Id
    @ManyToOne
    @JsonBackReference(value="course-user")
    private Course course;
}

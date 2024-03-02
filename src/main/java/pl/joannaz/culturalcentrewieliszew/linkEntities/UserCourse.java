package pl.joannaz.culturalcentrewieliszew.linkEntities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import pl.joannaz.culturalcentrewieliszew.course.Course;
import pl.joannaz.culturalcentrewieliszew.user.User;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="UserCourse") // join table between a user and a course
public class UserCourse {
    @Id
    @ManyToOne
    @JsonBackReference(value="user-course")
    private User participant;

    @Id
    @ManyToOne
    @JsonBackReference(value="course-user")
    private Course course;
}

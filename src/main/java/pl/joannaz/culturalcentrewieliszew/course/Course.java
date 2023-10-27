package pl.joannaz.culturalcentrewieliszew.course;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

import java.util.UUID;
@Entity(name="Course")
@Data
public class Course {
    @Id
    //@GeneratedValue(strategy = GenerationType.UUID)
    @SequenceGenerator(
            name = "course_seq",
            sequenceName = "course_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_seq")
    private UUID id;
    private String imgSource;
    private String name;
    private String teacher;
    private String description;

    public Course() {
        //this.id = UUID.randomUUID();
        this.imgSource = "";
        this.name = "";
        this.teacher = "";
        this.description = "";
    }
    public Course(String imgSource, String name, String teacher, String description) {
        //this.id = UUID.randomUUID();
        this.imgSource = imgSource;
        this.name = name;
        this.teacher = teacher;
        this.description = description;
    }
}

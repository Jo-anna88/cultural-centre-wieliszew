package pl.joannaz.culturalcentrewieliszew.course;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

import java.util.UUID;
/*
@Table(
        name="course",
        uniqueConstraints = @UniqueConstraint(name="course_name_unique", columnNames = "name")
) then we should remove 'unique=true' in @Column definition for name */
@Table(name="course")
@Entity(name="Course")
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    /*
    @SequenceGenerator(
            name = "course_seq",
            sequenceName = "course_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_seq")
    */
    @Column(
         name = "id",
         nullable = false,
         updatable = false
    )
    private UUID id;
    @Column(
            name="img_source",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String imgSource;
    @Column(
            name="name",
            nullable = false,
            columnDefinition = "TEXT",
            unique = true
    )
    private String name;
    private String teacher;
    @Column(
            name="description",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;

    public Course() {}
    public Course(String imgSource, String name, String teacher, String description) {
        this.id = UUID.randomUUID();
        this.imgSource = imgSource;
        this.name = name;
        this.teacher = teacher;
        this.description = description;
    }

    public Course(Course originalCourse) {
        this.id = originalCourse.id;
        this.imgSource = originalCourse.imgSource;
        this.name = originalCourse.name;
        this.teacher = originalCourse.teacher;
        this.description = originalCourse.description;
    }
}

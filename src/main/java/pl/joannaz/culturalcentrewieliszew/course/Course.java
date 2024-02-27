package pl.joannaz.culturalcentrewieliszew.course;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import pl.joannaz.culturalcentrewieliszew.linkEntities.UserCourse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
/*
@Table(
        name="course",
        uniqueConstraints = @UniqueConstraint(name="course_name_unique", columnNames = "name")
) then we should remove 'unique=true' in @Column definition for name */
@Table(name="course")
@Entity(name="Course")
@Data
//@JsonIgnoreProperties({"hibernateLazyInitializer"}) // for Unidirectional association for api/classes/{id}/details if we want to have course object nested in course details
//@JsonIdentityInfo( // for Bi-directional @OneToOne/@ManyToMany association (to remove recursion in JSON mapper)
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private Long id;

    @Column(
            name="img_source",
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

    private String teacher; // user_id that is a teacher (position)

    @Column(
            name="description",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(
            mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    //@JsonIgnoreProperties("course")
    //@JsonManagedReference
    private List<UserCourse> participants = new ArrayList<>();

//    @JsonIgnore // for Bi-directional @OneToOne association (to remove data about courseDetails during sending to frontend course object)
//    @OneToOne( // for Bi-directional @OneToOne association
//            mappedBy = "course",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true,
//            fetch = FetchType.LAZY
//    )
//    private CourseDetails courseDetails;

    public Course() {}
    public Course(String imgSource, String name, String teacher, String description) {
        //this.id = UUID.randomUUID();
        this.imgSource = imgSource;
        this.name = name;
        this.teacher = teacher;
        this.description = description;
        this.category = Category.OTHER; // default
    }
    public Course(String imgSource, String name, String teacher, String description, Category category) {
        this(imgSource, name, teacher, description);
        this.category = category;
    }

    // for Bi-directional @OneToOne association:
//    public Course(String imgSource, String name, String teacher, String description, Category category, CourseDetails courseDetails) {
//        this(imgSource, name, teacher, description, category);
//        this.addDetails(courseDetails);
//    }

    public Course(Course originalCourse) {
        this(
                originalCourse.imgSource,
                originalCourse.name,
                originalCourse.teacher,
                originalCourse.description,
                originalCourse.category
        );
        this.id = originalCourse.id;
    }

    // for Bi-directional @OneToOne association:
//    public void addDetails(CourseDetails courseDetails) {
//        courseDetails.setCourse(this);
//        this.courseDetails = courseDetails;
//    }
//
//    public void removeDetails() {
//        if (courseDetails != null) {
//            courseDetails.setCourse(null);
//            this.courseDetails = null;
//        }
//    }
}



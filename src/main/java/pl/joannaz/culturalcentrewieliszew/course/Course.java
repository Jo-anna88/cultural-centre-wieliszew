package pl.joannaz.culturalcentrewieliszew.course;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.joannaz.culturalcentrewieliszew.linkEntities.UserCourse;
import pl.joannaz.culturalcentrewieliszew.user.User;

import java.util.ArrayList;
import java.util.List;
/*
@Table(
        name="course",
        uniqueConstraints = @UniqueConstraint(name="course_name_unique", columnNames = "name")
) then we should remove 'unique=true' in @Column definition for name */
@Table(name="course")
@Entity(name="Course")
@Data
@NoArgsConstructor
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

    //private String teacher; // user_id that is a teacher (position)
    @ManyToOne(fetch = FetchType.LAZY)
    private User teacher;

    @Column(
            name="description",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(
            name="max_participants_number",
            nullable = false
    )
    private int maxParticipantsNumber; // maksymalna liczba uczestników

    @OneToMany(
            mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    //@JsonIgnoreProperties("course")
    @JsonManagedReference(value="course-user")
    private List<UserCourse> participants = new ArrayList<>(maxParticipantsNumber);

    public Course(String imgSource, String name, String description, int maxParticipantsNumber) {
        this.imgSource = imgSource;
        this.name = name;
        this.description = description;
        this.maxParticipantsNumber = maxParticipantsNumber;
        this.category = Category.OTHER; // default
    }

    public Course(String imgSource, String name, String description, int maxParticipantsNumber, Category category) {
        this(imgSource, name, description, maxParticipantsNumber);
        this.category = category;
    }

    public Course(String imgSource, String name, User teacher, String description, int maxParticipantsNumber) {
        this.imgSource = imgSource;
        this.name = name;
        this.teacher = teacher;
        this.description = description;
        this.maxParticipantsNumber = maxParticipantsNumber;
        this.category = Category.OTHER; // default
    }
    public Course(String imgSource, String name, User teacher, String description, int maxParticipantsNumber, Category category) {
        this(imgSource, name, teacher, description, maxParticipantsNumber);
        this.category = category;
    }

    public Course (CourseDTO courseDTO) {
        this(
                courseDTO.getImgSource(),
                courseDTO.getName(),
                // teacher set with usage of setter
                courseDTO.getDescription(),
                courseDTO.getMaxParticipantsNumber(),
                courseDTO.getCategory()
        );
        // id if update?
    }

    public Course(Course originalCourse) {
        this(
                originalCourse.imgSource,
                originalCourse.name,
                originalCourse.teacher,
                originalCourse.description,
                originalCourse.maxParticipantsNumber,
                originalCourse.category
        );
        this.id = originalCourse.id;
    }
}



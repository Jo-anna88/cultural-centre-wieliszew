package pl.joannaz.culturalcentrewieliszew.course;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import pl.joannaz.culturalcentrewieliszew.address.Address;

import java.math.BigDecimal;
@Entity
@Data
//@JsonIdentityInfo( // for Bi-directional @OneToOne association
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id")
public class CourseDetails {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // when we use @MapsId, it is visible in Persistence view, but not created in DB (column "id" does not exist); primary key (course_id)
    private int minAge;
    private int maxAge;
    private BigDecimal price; // cena za kurs / semestr
    private int maxParticipantsNumber; // maksymalna liczba uczestników
    private int lessonDurationMinutes; // czas trwania zajęć
    private String date; // termin, e.g., "Mon 13:00 - 13:45" (teoretycznie można tu wykorzystać informację o czasie trwania zajęć)

    @ManyToOne(fetch = FetchType.LAZY)  // "By default, @ManyToOne associations use the FetchType.EAGER strategy, which can lead to N+1 query issues or fetching more data than necessary"
    @JoinColumn(name = "address_id",
            foreignKey = @ForeignKey(name = "ADDRESS_ID_FK")
    )
    private Address address;

    //@ToString.Exclude // to deal with Stack Overflow Exception with toString() caused by recursion
    //@JoinColumn(name = "course_id") // for Bi-directional @OneToOne association
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JsonIgnore
    private Course course;

    public CourseDetails(int maxParticipantsNumber, BigDecimal price, Address address, int lessonDurationMinutes, int minAge, int maxAge, String date) {
        this.maxParticipantsNumber = maxParticipantsNumber;
        this.price = price;
        this.address = address;
        this.lessonDurationMinutes = lessonDurationMinutes;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.date = date;
    }

    public CourseDetails() {}

    public void update(CourseDetails updatedCourseDetails){
        setMinAge(updatedCourseDetails.getMinAge());
        setMaxAge(updatedCourseDetails.getMaxAge());
        setPrice(updatedCourseDetails.getPrice());
        setMaxParticipantsNumber(updatedCourseDetails.getMaxParticipantsNumber());
        setLessonDurationMinutes(updatedCourseDetails.getLessonDurationMinutes());
        setDate(updatedCourseDetails.getDate());
        setAddress(updatedCourseDetails.getAddress());
    }
}

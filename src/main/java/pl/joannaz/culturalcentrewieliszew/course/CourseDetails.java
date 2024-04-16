package pl.joannaz.culturalcentrewieliszew.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import pl.joannaz.culturalcentrewieliszew.address.Address;

import java.math.BigDecimal;
@Entity
@Data
public class CourseDetails {
    @Id
    private Long id; // when we use @MapsId, it is visible in Persistence view, but not created in DB (column "id" does not exist); primary key (course_id)
    private int minAge;
    private int maxAge;
    private BigDecimal price; // cena za kurs / semestr
    private int lessonDurationMinutes; // czas trwania zajęć
    private String date; // termin, e.g., "Mon 13:00 - 13:45" (teoretycznie można tu wykorzystać informację o czasie trwania zajęć)

    @ManyToOne(fetch = FetchType.LAZY)  // "By default, @ManyToOne associations use the FetchType.EAGER strategy, which can lead to N+1 query issues or fetching more data than necessary"
    @JoinColumn(name = "address_id",
            foreignKey = @ForeignKey(name = "ADDRESS_ID_FK")
    )
    private Address address;

    //@ToString.Exclude // to deal with Stack Overflow Exception with toString() caused by recursion
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JsonIgnore
    private Course course;

    public CourseDetails(int minAge, int maxAge, BigDecimal price, int lessonDurationMinutes, String date, Address address) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.price = price;
        this.lessonDurationMinutes = lessonDurationMinutes;
        this.date = date;
        this.address = address;
    }

    public CourseDetails() {}

    public CourseDetails(CourseDetailsDTO courseDetailsDTO) {
        this.minAge = courseDetailsDTO.getMinAge();
        this.maxAge = courseDetailsDTO.getMaxAge();
        this.price = courseDetailsDTO.getPrice();
        this.lessonDurationMinutes = courseDetailsDTO.getLessonDurationMinutes();
        this.date = courseDetailsDTO.getDate();
        // address set with usage of setter
        // course set with usage of setter
    }

    public void update(CourseDetailsDTO updatedCourseDetailsDTO) {
        this.minAge = updatedCourseDetailsDTO.getMinAge();
        this.maxAge = updatedCourseDetailsDTO.getMaxAge();
        this.price = updatedCourseDetailsDTO.getPrice();
        this.lessonDurationMinutes = updatedCourseDetailsDTO.getLessonDurationMinutes();
        this.date = updatedCourseDetailsDTO.getDate();
        // address set with usage of setter
        // course cannot be changed
    }
}

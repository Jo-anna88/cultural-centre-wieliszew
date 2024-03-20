package pl.joannaz.culturalcentrewieliszew.course;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.joannaz.culturalcentrewieliszew.address.Location;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CourseDetailsDTO {
    private Long id;
    private int minAge;
    private int maxAge;
    private BigDecimal price;
    private int lessonDurationMinutes;
    private String date;
    private Location location; // id needed to set corresponding address

    public CourseDetailsDTO (CourseDetails courseDetails) {
        this.id = courseDetails.getId();
        this.minAge = courseDetails.getMinAge();
        this.maxAge = courseDetails.getMaxAge();
        this.price = courseDetails.getPrice();
        this.lessonDurationMinutes = courseDetails.getLessonDurationMinutes();
        this.date = courseDetails.getDate();
        this.location = new Location(
                courseDetails.getAddress().getId(),
                courseDetails.getAddress().getLocation()
        );
    }
}

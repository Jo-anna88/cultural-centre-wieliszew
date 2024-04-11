package pl.joannaz.culturalcentrewieliszew.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseBasicInfo {
    private Long id;
    private String name;

    public CourseBasicInfo(Course course) {
        this.id = course.getId();
        this.name = course.getName();
    }
}

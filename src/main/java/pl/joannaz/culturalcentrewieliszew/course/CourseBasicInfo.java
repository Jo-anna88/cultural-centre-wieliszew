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
}

package pl.joannaz.culturalcentrewieliszew.course;

import lombok.Data;

@Data
public class CourseDTO {
    private Long id;
    private String imgSource;
    private String name;;
    private String teacher;
    private String description;
    private Category category;

    public CourseDTO (Course course) {
        this.id = course.getId();
        this.imgSource = course.getImgSource();
        this.name = course.getName();
        this.teacher = course.getTeacher();
        this.description = course.getDescription();
        this.category = course.getCategory();
    }
}

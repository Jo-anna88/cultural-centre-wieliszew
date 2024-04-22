package pl.joannaz.culturalcentrewieliszew.course;

import lombok.Data;
import pl.joannaz.culturalcentrewieliszew.user.Role;
import pl.joannaz.culturalcentrewieliszew.user.UserBasicInfo;

import java.io.Serializable;

@Data
public class CourseDTO implements Serializable {
    private Long id;
    private String imgSource;
    private String name;
    private UserBasicInfo teacher;
    private String description;
    private Category category;
    private int maxParticipantsNumber;
    private int freeSlots;

    public CourseDTO () {}

    public CourseDTO (Course course) {
        this.id = course.getId();
        this.imgSource = course.getImgSource();
        this.name = course.getName();
        if(course.getTeacher() != null) {
            this.teacher = new UserBasicInfo(
                    course.getTeacher().getId(),
                    course.getTeacher().getFirstName(),
                    course.getTeacher().getLastName());
        }
        this.description = course.getDescription();
        this.category = course.getCategory();
        this.maxParticipantsNumber = course.getMaxParticipantsNumber();
        this.freeSlots = maxParticipantsNumber - course.getParticipants().size();
    }
}

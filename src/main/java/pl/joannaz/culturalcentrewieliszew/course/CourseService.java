package pl.joannaz.culturalcentrewieliszew.course;


import org.springframework.stereotype.Service;

import java.util.List;

import static pl.joannaz.culturalcentrewieliszew.utils.constants.SIMPLE_TEXT;

@Service
public class CourseService {
    public List<Course> getAllCourses() {
        return List.of(
                new Course("assets/icons/ballet-shoes.png", "Ballet", "Anna Baletowicz", SIMPLE_TEXT),
                new Course("assets/icons/chess.png", "Chess", "Igor Szachista", SIMPLE_TEXT),
                new Course("assets/icons/guitar.png", "Guitar", "Jan Muzyk", SIMPLE_TEXT),
                new Course("assets/icons/pottery.png", "Pottery", "Katarzyna Waza", SIMPLE_TEXT),
                new Course("assets/icons/theatre.png", "Theatre", "Agnieszka Teatralna", SIMPLE_TEXT),
                new Course("assets/icons/microphone.png", "Vocal", "Łukasz Wokalista", SIMPLE_TEXT)
        );
    }
}

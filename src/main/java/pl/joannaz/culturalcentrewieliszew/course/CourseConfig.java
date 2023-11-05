package pl.joannaz.culturalcentrewieliszew.course;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static pl.joannaz.culturalcentrewieliszew.utils.constants.SIMPLE_TEXT;

@Configuration
public class CourseConfig {

    @Bean
    CommandLineRunner clr (CourseRepository courseRepository) {
		return args -> {
			courseRepository.saveAll(
                List.of(
                    new Course("assets/icons/ballet-shoes.png", "Ballet", "Anna Baletowicz", SIMPLE_TEXT),
                    new Course("assets/icons/chess.png", "Chess", "Igor Szachista", SIMPLE_TEXT),
                    new Course("assets/icons/guitar.png", "Guitar", "Jan Muzyk", SIMPLE_TEXT),
                    new Course("assets/icons/pottery.png", "Pottery", "Katarzyna Waza", SIMPLE_TEXT),
                    new Course("assets/icons/theatre.png", "Theatre", "Agnieszka Teatralna", SIMPLE_TEXT),
                    new Course("assets/icons/microphone.png", "Vocal", "Łukasz Wokalista", SIMPLE_TEXT)
                )
            );
		};
	}

}

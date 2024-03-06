package pl.joannaz.culturalcentrewieliszew.course;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.joannaz.culturalcentrewieliszew.models.Address;

import java.math.BigDecimal;
import java.util.List;

import static pl.joannaz.culturalcentrewieliszew.utils.constants.SIMPLE_TEXT;

@Configuration
public class CourseConfig {

    @Bean
    CommandLineRunner clr (CourseRepository courseRepository) {
		return args -> {
            CourseDetails cd1 = new CourseDetails(
                //20,
                BigDecimal.valueOf(150.00),
                new Address("Wieliszew", "05-135", "Kulturalna", "7", 10),
                45,
                5,
                8,
                "Mon 14:00 - 14:45"
            );
			courseRepository.saveAll(
                List.of(
                    new Course("assets/icons/ballet-shoes.png", "Ballet", "Anna Baletowicz", SIMPLE_TEXT, 10, Category.DANCE),
                    new Course("assets/icons/chess.png", "Chess", "Igor Szachista", SIMPLE_TEXT, 20, Category.SPORT),
                    new Course("assets/icons/guitar.png", "Guitar", "Jan Muzyk", SIMPLE_TEXT, 2, Category.MUSIC),
                    new Course("assets/icons/pottery.png", "Pottery", "Katarzyna Waza", SIMPLE_TEXT, 15, Category.ART),
                    new Course("assets/icons/theatre.png", "Theatre", "Agnieszka Teatralna", SIMPLE_TEXT, 25, Category.ART),
                    new Course("assets/icons/microphone.png", "Vocal", "Łukasz Wokalista", SIMPLE_TEXT, 5, Category.MUSIC),
                    new Course("assets/icons/default.png", "Python", "Piotr Programista", SIMPLE_TEXT, 10, Category.EDUCATION)
                )
            );
		};
	}

}

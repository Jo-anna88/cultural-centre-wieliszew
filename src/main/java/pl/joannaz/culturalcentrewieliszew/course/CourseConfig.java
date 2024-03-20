package pl.joannaz.culturalcentrewieliszew.course;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import pl.joannaz.culturalcentrewieliszew.user.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static pl.joannaz.culturalcentrewieliszew.utils.constants.SIMPLE_TEXT;

@Configuration
public class CourseConfig {

    @Bean
    CommandLineRunner courseRunner (CourseRepository courseRepository,
                                    CourseService courseService,
                                    CourseDetailsRepository detailsRepository,
                                    UserRepository userRepository) {
		return args -> {
            Thread.sleep(3000);
            Course course1 = new Course("assets/icons/ballet-shoes.png", "Ballet",
                    userRepository.findByUsername("anna.baletowicz@ccw.pl").get(),
                    SIMPLE_TEXT, 10, Category.DANCE);
			courseRepository.saveAll(
                List.of(
                    course1,
                    new Course("assets/icons/chess.png", "Chess",
                            userRepository.findByUsername("igor.szachista@ccw.pl").get(),
                            SIMPLE_TEXT, 20, Category.SPORT),
                    new Course("assets/icons/guitar.png", "Guitar",
                            userRepository.findByUsername("jan.muzyk@ccw.pl").get(),
                            SIMPLE_TEXT, 2, Category.MUSIC),
                    new Course("assets/icons/pottery.png", "Pottery",
                            userRepository.findByUsername("katarzyna.waza@ccw.pl").get(),
                            SIMPLE_TEXT, 15, Category.ART),
                    new Course("assets/icons/theatre.png", "Theatre",
                            userRepository.findByUsername("agnieszka.teatralna@ccw.pl").get(),
                            SIMPLE_TEXT, 25, Category.ART),
                    new Course("assets/icons/microphone.png", "Vocal",
                            userRepository.findByUsername("lukasz.wokalista@ccw.pl").get(),
                            SIMPLE_TEXT, 5, Category.MUSIC),
                    new Course("assets/icons/default.png", "Python",
                            userRepository.findByUsername("piotr.programista@ccw.pl").get(),
                            SIMPLE_TEXT, 10, Category.EDUCATION)
                )
            );
//            CourseDetails courseDetails1 = new CourseDetails(5, 8, BigDecimal.valueOf(150.00),
//                    45, "Mon 14:00 - 14:45", courseService.getAddressById(1));
//            courseDetails1.setCourse(course1);
//            detailsRepository.save(courseDetails1);
		};
	}

}

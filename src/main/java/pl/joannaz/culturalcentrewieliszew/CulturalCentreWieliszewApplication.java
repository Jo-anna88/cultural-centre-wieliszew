package pl.joannaz.culturalcentrewieliszew;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.joannaz.culturalcentrewieliszew.course.Course;
import pl.joannaz.culturalcentrewieliszew.course.CourseRepository;

import static pl.joannaz.culturalcentrewieliszew.utils.constants.SIMPLE_TEXT;

@SpringBootApplication
public class CulturalCentreWieliszewApplication {
	public static void main(String[] args) {
		SpringApplication.run(CulturalCentreWieliszewApplication.class, args);
	}

//	@Bean
//	CommandLineRunner clr (CourseRepository courseRepository) {
//		return args -> {
//			Course c1 = new Course("assets/icons/ballet-shoes.png", "Ballet", "Anna Baletowicz", SIMPLE_TEXT);
//			courseRepository.save(c1);
//		};
//	}

}

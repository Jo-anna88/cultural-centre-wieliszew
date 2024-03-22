package pl.joannaz.culturalcentrewieliszew.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.UUID;

@Configuration
public class UserConfig {
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    CommandLineRunner userRunner (UserRepository userRepository) {
        return args -> userRepository.saveAll(
            List.of(
                    new User( // admin
                            "Paweł",
                            "Zzz",
                            "123-456-789",
                            "1981-01-01",
                            "assets/images/avatar3.svg",
                            Role.ADMIN,
                            "Director",
                            "description"
                    ),
                    new User( // parent1
                            "Joanna",
                            "Zzz",
                            "123-456-789",
                            "joanna.zzz@gmail.com",
                            "$2a$10$vUQaRwWe6Km1cZk9T0.3OeVsgY/1X0NY.ngLlYOzkT91hubu3fDb.",
                            "1988-03-01",
                            "assets/images/avatar4.svg"
                    ),
                    new User( // parent2
                            "Franek",
                            "Aaa",
                            "123-456-789",
                            "franek.aaa@gmail.com",
                            "$2a$10$vUQaRwWe6Km1cZk9T0.3OeVsgY/1X0NY.ngLlYOzkT91hubu3fDb.",
                            "1975-12-20",
                            "assets/images/avatar1.svg"
                    ),
                    new User( // employee1
                            "Anna",
                            "Baletowicz",
                            "789-111-111",
                            "2000-01-01",
                            "assets/images/avatar2.svg",
                            Role.EMPLOYEE,
                            "teacher",
                            "Lorem ipsum"
                    ),
                    new User( // employee2
                            "Igor",
                            "Szachista",
                            "789-222-222",
                            "2000-02-02",
                            "assets/images/avatar1.svg",
                            Role.EMPLOYEE,
                            "teacher",
                            "Lorem ipsum"
                    ),
                    new User( // employee3
                            "Jan",
                            "Muzyk",
                            "789-333-333",
                            "2000-03-03",
                            "assets/images/avatar1.svg",
                            Role.EMPLOYEE,
                            "teacher",
                            "Lorem ipsum"
                    ),
                    new User( // employee4
                            "Katarzyna",
                            "Waza",
                            "789-444-444",
                            "2000-04-04",
                            "assets/images/avatar2.svg",
                            Role.EMPLOYEE,
                            "teacher",
                            "Lorem ipsum"
                    ),
                    new User( // employee5
                            "Agnieszka",
                            "Teatralna",
                            "789-555-555",
                            "2000-05-05",
                            "assets/images/avatar2.svg",
                            Role.EMPLOYEE,
                            "teacher",
                            "Lorem ipsum"
                    ),
                    new User( // employee6
                            "Lukasz",
                            "Wokalista",
                            "789-222-222",
                            "2000-06-06",
                            "assets/images/avatar1.svg",
                            Role.EMPLOYEE,
                            "teacher",
                            "Lorem ipsum"
                    ),
                    new User( // employee7
                            "Piotr",
                            "Programista",
                            "789-777-777",
                            "2000-07-07",
                            "assets/images/avatar1.svg",
                            Role.EMPLOYEE,
                            "teacher",
                            "Lorem ipsum"
                    )
            )
        );
    }

    @Bean
    CommandLineRunner childUserRunner (UserRepository userRepository) {
        return args -> userRepository.saveAll(
                List.of(
                    new User( // child1.1
                            userRepository.findByUsername("joanna.zzz@gmail.com").get().getId(),
                            "Iga",
                            "Zzz",
                            "j@gmail.com/IgaZzz",
                            "2010-04-01",
                            "assets/images/avatar-girl.svg"
                    ),
                    new User( // child1.2
                            userRepository.findByUsername("joanna.zzz@gmail.com").get().getId(),
                            "Franek",
                            "Zzz",
                            "j@gmail.com/FranekZzz",
                            "2012-05-01",
                            "assets/images/avatar-boy.svg"
                    ),
                    new User( // child1.3
                            userRepository.findByUsername("joanna.zzz@gmail.com").get().getId(),
                            "Piotr",
                            "Zzz",
                            "j@gmail.com/PiotrZzz",
                            "2014-07-01",
                            "assets/images/avatar-boy.svg"
                    ),
                    new User( // child2.1
                            userRepository.findByUsername("franek.aaa@gmail.com").get().getId(),
                            "Lukasz",
                            "Aaa",
                            "szymon@gmail.com/LukaszAaa",
                            "2000-03-15",
                            "assets/images/avatar-boy.svg"
                    ),
                    new User( // child2.2
                            userRepository.findByUsername("franek.aaa@gmail.com").get().getId(),
                            "Zofia",
                            "Aaa",
                            "szymon@gmail.com/ZofiaAaa",
                            "2004-11-25",
                            "assets/images/avatar-girl.svg"
                    )
                )
        );
    }
}

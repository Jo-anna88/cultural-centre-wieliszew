package pl.joannaz.culturalcentrewieliszew.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.UUID;

import static pl.joannaz.culturalcentrewieliszew.utils.constants.SIMPLE_TEXT_SHORT;

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
                            Role.ADMIN,
                            "Director",
                            SIMPLE_TEXT_SHORT
                    ),
                    new User( // parent1
                            "Joanna",
                            "Zzz",
                            "123-456-789",
                            "joanna.zzz@gmail.com",
                            "1988-03-01",
                            "assets/images/avatar4.svg"
                    ),
                    new User( // parent2
                            "Franek",
                            "Aaa",
                            "123-456-789",
                            "franek.aaa@gmail.com",
                            "1975-12-20",
                            "assets/images/avatar3.svg"
                    ),
                    new User( // employee1
                            "Anna",
                            "Baletowicz",
                            "789-111-111",
                            "2000-01-01",
                            Role.EMPLOYEE,
                            "Teacher",
                            SIMPLE_TEXT_SHORT
                    ),
                    new User( // employee2
                            "Igor",
                            "Szachista",
                            "789-222-222",
                            "2000-02-02",
                            Role.EMPLOYEE,
                            "Teacher",
                            SIMPLE_TEXT_SHORT
                    ),
                    new User( // employee3
                            "Jan",
                            "Muzyk",
                            "789-333-333",
                            "2000-03-03",
                            Role.EMPLOYEE,
                            "Teacher",
                            SIMPLE_TEXT_SHORT
                    ),
                    new User( // employee4
                            "Katarzyna",
                            "Waza",
                            "789-444-444",
                            "2000-04-04",
                            Role.EMPLOYEE,
                            "Teacher",
                            SIMPLE_TEXT_SHORT
                    ),
                    new User( // employee5
                            "Agnieszka",
                            "Teatralna",
                            "789-555-555",
                            "2000-05-05",
                            Role.EMPLOYEE,
                            "Teacher",
                            SIMPLE_TEXT_SHORT
                    ),
                    new User( // employee6
                            "Lukasz",
                            "Wokalista",
                            "789-222-222",
                            "2000-06-06",
                            Role.EMPLOYEE,
                            "Teacher",
                            SIMPLE_TEXT_SHORT
                    ),
                    new User( // employee7
                            "Piotr",
                            "Programista",
                            "789-777-777",
                            "2000-07-07",
                            Role.EMPLOYEE,
                            "Teacher",
                            SIMPLE_TEXT_SHORT
                    )
            )
        );
    }

    @Bean
    CommandLineRunner childUserRunner (UserRepository userRepository) {
        return args -> userRepository.saveAll(
                List.of(
                    new User( // child1.1
                            userRepository.findByUsername("joanna.zzz@gmail.com").get(),
                            "Iga",
                            "Zzz",
                            userRepository.findByUsername("joanna.zzz@gmail.com").get().getPhone(),
                            "joanna.zzz@gmail.com/IgaZzz",
                            "2010-04-01",
                            "assets/images/avatar-girl.svg"
                    ),
                    new User( // child1.2
                            userRepository.findByUsername("joanna.zzz@gmail.com").get(),
                            "Maksymilian",
                            "Zzz",
                            userRepository.findByUsername("joanna.zzz@gmail.com").get().getPhone(),
                            "joanna.zzz@gmail.com/MaksymilianZzz",
                            "2012-05-01",
                            "assets/images/avatar-boy.svg"
                    ),
                    new User( // child1.3
                            userRepository.findByUsername("joanna.zzz@gmail.com").get(),
                            "Piotr",
                            "Zzz",
                            userRepository.findByUsername("joanna.zzz@gmail.com").get().getPhone(),
                            "joanna.zzz@gmail.com/PiotrZzz",
                            "2014-07-01",
                            "assets/images/avatar-boy.svg"
                    ),
                    new User( // child2.1
                            userRepository.findByUsername("franek.aaa@gmail.com").get(),
                            "Lukasz",
                            "Aaa",
                            userRepository.findByUsername("joanna.zzz@gmail.com").get().getPhone(),
                            "franek.aaa@gmail.com/LukaszAaa",
                            "2000-03-15",
                            "assets/images/avatar-boy.svg"
                    ),
                    new User( // child2.2
                            userRepository.findByUsername("franek.aaa@gmail.com").get(),
                            "Zofia",
                            "Aaa",
                            userRepository.findByUsername("joanna.zzz@gmail.com").get().getPhone(),
                            "franek.aaa@gmail.com/ZofiaAaa",
                            "2004-11-25",
                            "assets/images/avatar-girl.svg"
                    )
                )
        );
    }
}

package pl.joannaz.culturalcentrewieliszew.address;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AddressConfig {
    @Bean
    CommandLineRunner addressRunner (AddressRepository addressRepository) {
        return args -> addressRepository.saveAll(
                List.of(
                    new Address(
                        1, "CCW headquarters - Wieliszew", "Wieliszew", "05-135", "Kulturalna", "1"
                    ),
                    new Address(
                        2, "CCW branch - Olszewnica", "Olszewnica", "05-135", "Artystyczna", "2"
                    ),
                    new Address(
                        3, "CCW branch - Skrzeszew", "Skrzeszew", "05-135", "Teatralna", "3"
                    ),
                    new Address(
                        4, "CCW branch - Komornica", "Komornica", "05-135", "Sportowa", "4"
                    )
                )
            );
    }


}

package pl.joannaz.culturalcentrewieliszew.culturalevent;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.joannaz.culturalcentrewieliszew.address.AddressRepository;

@Configuration
public class CulturalEventConfig {

    @Bean
    CommandLineRunner culturalEventRunner (CulturalEventRepository culturalEventRepository, AddressRepository addressRepository) {
        return args -> {
            Thread.sleep(3000);
            culturalEventRepository.saveAll(MockCulturalEvent.culturalEventList);
        };
    }
}

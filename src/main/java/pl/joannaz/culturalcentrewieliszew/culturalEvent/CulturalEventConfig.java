package pl.joannaz.culturalcentrewieliszew.culturalEvent;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.joannaz.culturalcentrewieliszew.address.AddressRepository;
import pl.joannaz.culturalcentrewieliszew.address.Location;

import java.math.BigDecimal;
import java.util.List;

import static pl.joannaz.culturalcentrewieliszew.utils.constants.SIMPLE_TEXT;
import static pl.joannaz.culturalcentrewieliszew.utils.constants.SIMPLE_TEXT_SHORT;

@Configuration
public class CulturalEventConfig {

    @Bean
    CommandLineRunner culturalEventRunner (CulturalEventRepository culturalEventRepository, AddressRepository addressRepository) {
        return args -> {
            Thread.sleep(3000);
            culturalEventRepository.saveAll(
                    List.of(
                            new CulturalEvent(
                                    "assets/images/cultural-event1.jpg",
                                    "Event1",
                                    "2024-07-07",
                                    SIMPLE_TEXT,
                                    BigDecimal.valueOf(25),
                                    addressRepository.findById(1).get()),
                            new CulturalEvent(
                                    "assets/images/cultural-event2.jpg",
                                    "Event2",
                                    "2024-11-20",
                                    SIMPLE_TEXT,
                                    BigDecimal.valueOf(50),
                                    addressRepository.findById(2).get()),
                            new CulturalEvent(
                                    "assets/images/cultural-event3.jpg",
                                    "Event3",
                                    "2024-08-10",
                                    SIMPLE_TEXT,
                                    BigDecimal.valueOf(75),
                                    addressRepository.findById(3).get()),
                            new CulturalEvent(
                                    "assets/images/cultural-event-default.jpg",
                                    "Event4",
                                    "2024-09-13",
                                    SIMPLE_TEXT,
                                    BigDecimal.valueOf(100),
                                    addressRepository.findById(4).get())
                    )
            );
        };
    }
}

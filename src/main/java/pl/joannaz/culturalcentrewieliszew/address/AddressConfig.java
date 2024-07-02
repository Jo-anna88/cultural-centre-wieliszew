package pl.joannaz.culturalcentrewieliszew.address;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AddressConfig {
    @Bean
    CommandLineRunner addressRunner (AddressRepository addressRepository) {
        return args -> addressRepository.saveAll(MockAddress.addressList);
    }
}

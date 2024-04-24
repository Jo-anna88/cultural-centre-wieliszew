package pl.joannaz.culturalcentrewieliszew.address;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);

    public AddressService(AddressRepository addressRepository) {this.addressRepository = addressRepository;}
    public List<Address> getAddresses() {
        // throw new RuntimeException("test message");
        logger.info("Fetching all courses.");
        return addressRepository.findAll();
    }
    public List<Location> getLocations() {
        logger.info("Fetching all locations.");
        List<Object[]> locations = addressRepository.findAllLocations();
        return locations.stream()
                .map(result -> new Location((Integer)result[0], (String) result[1]))
                .collect(Collectors.toList());
    }
}

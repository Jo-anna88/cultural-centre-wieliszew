package pl.joannaz.culturalcentrewieliszew.address;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {this.addressRepository = addressRepository;}
    public List<Address> getAddresses() {
        return addressRepository.findAll();
    }
    public List<Location> getLocations() {
        List<Object[]> locations = addressRepository.findAllLocations();
        return locations.stream()
                .map(result -> new Location((Integer)result[0], (String) result[1]))
                .collect(Collectors.toList());
    }
}

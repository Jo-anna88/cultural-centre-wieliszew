package pl.joannaz.culturalcentrewieliszew.address;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/api/address")
public class AddressController {

    private final AddressService addressService;
    public AddressController(AddressService addressService) {this.addressService = addressService;}

    @GetMapping
    public List<Address> getAddresses() {return addressService.getAddresses();}

    @GetMapping("/locations")
    public List<Location> getLocations() {return addressService.getLocations();}
}

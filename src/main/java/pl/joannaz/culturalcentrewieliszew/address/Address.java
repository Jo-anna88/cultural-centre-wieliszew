package pl.joannaz.culturalcentrewieliszew.address;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "address")
public class Address {
    @Id
    private Long id;
    private String city;
    private String zipCode;
    private String street;
    private String houseNumber;
    private int flatNumber; // or room

    public Address(String city, String zipCode, String street, String houseNumber, int flatNumber) {
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
        this.houseNumber = houseNumber;
        this.flatNumber = flatNumber;
    }
}

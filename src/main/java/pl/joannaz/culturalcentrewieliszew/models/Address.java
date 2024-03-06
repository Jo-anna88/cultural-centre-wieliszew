package pl.joannaz.culturalcentrewieliszew.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity(name = "address")
public class Address {
    @Id
    private Long id;
    private String city;
    private String zipCode;
    private String street;
    private String houseNumber;
    private int flatNumber; // default value for declared field of type int: 0

    public Address(String city, String zipCode, String street, String houseNumber) {
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
        this.houseNumber = houseNumber;
    }

    public Address(String city, String zipCode, String street, String houseNumber, int flatNumber) {
        this(city, zipCode, street, houseNumber);
        this.flatNumber = flatNumber;
    }
}
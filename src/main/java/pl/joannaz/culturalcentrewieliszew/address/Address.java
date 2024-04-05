package pl.joannaz.culturalcentrewieliszew.address;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(
        name="address",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "location")
        })
@Entity(name = "address")
public class Address {
    @Id
    private Integer id;
    private String location; // name of location (headquarters, branches)
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

    public Address(int id, String location, String city, String zipCode, String street, String houseNumber) {
        this(city, zipCode, street, houseNumber);
        this.location = location;
        this.id = id;
    }

    public Address(int id, String location, String city, String zipCode, String street, String houseNumber, int flatNumber) {
        this(id, location, city, zipCode, street, houseNumber);
        this.flatNumber = flatNumber;
    }
}
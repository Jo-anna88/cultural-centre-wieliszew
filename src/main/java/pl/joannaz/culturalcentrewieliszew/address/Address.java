package pl.joannaz.culturalcentrewieliszew.address;

import jakarta.persistence.*;
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

    @Column(
            name="location",
            nullable = false
    )
    private String location; // name of location (headquarters, branches)

    @Column(
            name="city",
            nullable = false
    )
    private String city;

    @Column(
            name="zip_code",
            nullable = false
    )
    private String zipCode;

    @Column(
            name="street",
            nullable = false
    )
    private String street;

    @Column(
            name="house_number",
            nullable = false
    )
    private String houseNumber;

    @Column(
            name="flat_number"
    )
    private Integer flatNumber; // default value for declared field of type int: 0

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
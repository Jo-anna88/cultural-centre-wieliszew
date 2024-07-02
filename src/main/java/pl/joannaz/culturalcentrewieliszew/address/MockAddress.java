package pl.joannaz.culturalcentrewieliszew.address;

import pl.joannaz.culturalcentrewieliszew.address.Address;

import java.util.List;

// address sample data
public class MockAddress {
    public static List<Address> addressList = List.of(
            new Address(
                        1, "CCW headquarters - Wieliszew", "Wieliszew", "05-135", "Kulturalna", "1"
    ),
                    new Address(
                        2, "CCW branch - Olszewnica", "Olszewnica", "05-135", "Artystyczna", "2"
    ),
                    new Address(
                        3, "CCW branch - Skrzeszew", "Skrzeszew", "05-135", "Teatralna", "3"
    ),
                    new Address(
                        4, "CCW branch - Komornica", "Komornica", "05-135", "Sportowa", "4"
    )
                );
}

package pl.joannaz.culturalcentrewieliszew.address;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@AllArgsConstructor
public class Location {
    private Integer id;
    private String location;

    public Location(Integer id, String location) {
        this.id=id;
        this.location = location;
    }
}

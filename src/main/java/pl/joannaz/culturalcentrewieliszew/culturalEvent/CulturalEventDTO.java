package pl.joannaz.culturalcentrewieliszew.culturalEvent;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.joannaz.culturalcentrewieliszew.address.Location;

@Data
@NoArgsConstructor
public class CulturalEventDTO {
    private Long id;
    private String imgSource;
    private String name;
    private String date;
    private String description;
    private Location location;

    public CulturalEventDTO (CulturalEvent culturalEvent) {
        this.id = culturalEvent.getId();
        this.imgSource = culturalEvent.getImgSource();
        this.name = culturalEvent.getName();
        this.date = culturalEvent.getDate();
        this.description = culturalEvent.getDescription();
        this.location = new Location(
                culturalEvent.getAddress().getId(),
                culturalEvent.getAddress().getLocation()
        );
    }
}

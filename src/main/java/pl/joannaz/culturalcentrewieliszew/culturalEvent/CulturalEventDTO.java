package pl.joannaz.culturalcentrewieliszew.culturalEvent;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.joannaz.culturalcentrewieliszew.address.Location;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CulturalEventDTO {
    private Long id;
    private String imgSource;
    private String name;
    private LocalDate date;
    private String description;
    private BigDecimal price;
    private Location location;

    public CulturalEventDTO (CulturalEvent culturalEvent) {
        this.id = culturalEvent.getId();
        this.imgSource = culturalEvent.getImgSource();
        this.name = culturalEvent.getName();
        this.date = culturalEvent.getDate();
        this.description = culturalEvent.getDescription();
        this.price = culturalEvent.getPrice();
        this.location = new Location(
                culturalEvent.getAddress().getId(),
                culturalEvent.getAddress().getLocation()
        );
    }
}

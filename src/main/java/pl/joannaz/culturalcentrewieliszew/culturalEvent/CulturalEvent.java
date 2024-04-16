package pl.joannaz.culturalcentrewieliszew.culturalEvent;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.joannaz.culturalcentrewieliszew.address.Address;
import pl.joannaz.culturalcentrewieliszew.linkEntities.UserCulturalEvent;
import pl.joannaz.culturalcentrewieliszew.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data // for setters and getters
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="CulturalEvent")
public class CulturalEvent {
    @Id
    private Long id;
    private String imgSource;
    private String name;
    private String date;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)  // "By default, @ManyToOne associations use the FetchType.EAGER strategy, which can lead to N+1 query issues or fetching more data than necessary"
    @JoinColumn(name = "address_id",
            foreignKey = @ForeignKey(name = "ADDRESS_ID_FK")
    )
    private Address address;

    public CulturalEvent(CulturalEventDTO culturalEventDTO) {
        this.imgSource = culturalEventDTO.getImgSource();
        this.name = culturalEventDTO.getName();
        this.date = culturalEventDTO.getDate();
        this.description = culturalEventDTO.getDescription();
        // address set with usage of setter
    }
}

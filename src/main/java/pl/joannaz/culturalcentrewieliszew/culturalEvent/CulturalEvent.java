package pl.joannaz.culturalcentrewieliszew.culturalEvent;

import jakarta.persistence.*;
import lombok.Data;
import pl.joannaz.culturalcentrewieliszew.linkEntities.UserCulturalEvent;
import pl.joannaz.culturalcentrewieliszew.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data // for setters and getters
@Entity(name="CulturalEvent")
public class CulturalEvent {
    @Id
    private Long id;
    String date;
    int place; // nr sali (?)
    int tickets; // number of tickets
    String description;
    //@ManyToMany(mappedBy = "culturalEvents")
    //Set<User> participants;
    @OneToMany(
            mappedBy = "culturalEvent",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<UserCulturalEvent> participants = new ArrayList<>();
}

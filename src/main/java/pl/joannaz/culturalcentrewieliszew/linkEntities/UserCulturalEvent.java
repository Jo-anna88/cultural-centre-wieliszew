package pl.joannaz.culturalcentrewieliszew.linkEntities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.joannaz.culturalcentrewieliszew.culturalEvent.CulturalEvent;
import pl.joannaz.culturalcentrewieliszew.user.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="UserCulturalEvent") // join table between a user and a cultural event
public class UserCulturalEvent {
    @Id
    @ManyToOne
    private User participant;

    @Id
    @ManyToOne
    private CulturalEvent culturalEvent;
}

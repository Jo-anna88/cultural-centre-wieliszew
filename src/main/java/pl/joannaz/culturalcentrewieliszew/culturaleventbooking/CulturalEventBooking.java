package pl.joannaz.culturalcentrewieliszew.culturaleventbooking;

import jakarta.persistence.*;
import lombok.*;
import pl.joannaz.culturalcentrewieliszew.culturalevent.CulturalEvent;
import pl.joannaz.culturalcentrewieliszew.user.User;

@Table(name = "cultural_event_booking")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="CulturalEventBooking") // join table between a user and a cultural event
public class CulturalEventBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name="participant_id",
            nullable = false
    )
    private User participant;

    @ManyToOne
    @JoinColumn(
            name="cultural_event_id",
            nullable = false
    )
    private CulturalEvent culturalEvent;

    @Column (
            name="number_of_tickets",
            nullable = false
    )
    private Integer numberOfTickets;

    public CulturalEventBooking(User participant, CulturalEvent culturalEvent, Integer numberOfTickets) {
        this.participant = participant;
        this.culturalEvent = culturalEvent;
        this.numberOfTickets = numberOfTickets;
    }
}

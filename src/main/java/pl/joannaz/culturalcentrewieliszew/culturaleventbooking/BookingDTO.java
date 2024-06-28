package pl.joannaz.culturalcentrewieliszew.culturaleventbooking;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class BookingDTO {
    private Long id;
    private UUID participantId;
    private Long culturalEventId;
    private Integer numberOfTickets;

    public BookingDTO (CulturalEventBooking booking) {
        this.id = booking.getId();
        this.participantId = booking.getParticipant().getId();
        this.culturalEventId = booking.getCulturalEvent().getId();
        this.numberOfTickets = booking.getNumberOfTickets();
    }
}

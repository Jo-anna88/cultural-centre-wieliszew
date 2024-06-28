package pl.joannaz.culturalcentrewieliszew.culturaleventbooking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<CulturalEventBooking, Long> {
    List<CulturalEventBooking> findAllByParticipantId(UUID participantId);

    @Query("SELECT SUM(booking.numberOfTickets) FROM CulturalEventBooking booking WHERE booking.culturalEvent.id = :eventId")
    Integer countBookedTickets(@Param("eventId") Long eventId);
}

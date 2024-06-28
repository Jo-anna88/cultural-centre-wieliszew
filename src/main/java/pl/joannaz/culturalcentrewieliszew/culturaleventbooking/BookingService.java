package pl.joannaz.culturalcentrewieliszew.culturaleventbooking;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.joannaz.culturalcentrewieliszew.culturalevent.CulturalEvent;
import pl.joannaz.culturalcentrewieliszew.culturalevent.CulturalEventRepository;
import pl.joannaz.culturalcentrewieliszew.culturalevent.CulturalEventService;
import pl.joannaz.culturalcentrewieliszew.user.User;
import pl.joannaz.culturalcentrewieliszew.user.UserService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {
    private final BookingRepository bookingRepository;
    private final CulturalEventService culturalEventService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    public List<BookingDTO> getBookingsByUserId(UUID id) {
        logger.info("Fetching all courses for user {}.", id);
        return bookingRepository.findAllByParticipantId(id).stream()
                .map(BookingDTO::new)
                .collect(Collectors.toList());
    }

    public BookingDTO bookTicket(BookingDTO bookingDTO) {
        // Fetch cultural event
        CulturalEvent culturalEvent = culturalEventService.getCulturalEvent(bookingDTO.getCulturalEventId());

        // Check free slots for cultural event
        logger.info("Checking free slots for the cultural event with id: {}.", bookingDTO.getCulturalEventId());
        int freeSlots = getFreeSlots(culturalEvent.getId(), culturalEvent.getMaxParticipantsNumber());
        boolean isEnoughFreeSlots = freeSlots >= bookingDTO.getNumberOfTickets();

        // Add new booking
        if (isEnoughFreeSlots) {
            logger.info("Adding new booking: {}", bookingDTO);
            User participant = userService.getCurrentUser();
            CulturalEventBooking booking = new CulturalEventBooking(
                    participant, culturalEvent, bookingDTO.getNumberOfTickets()
            );
            return new BookingDTO(bookingRepository.save(booking));
        } else {
            logger.error("There is {} free slots for cultural event with id: {}.", freeSlots, culturalEvent.getId());
            throw new RuntimeException(String.format("Sorry, there is not enough free slots for this event." +
                    "There is %s free slots now.", freeSlots));
        }
    }

    public int getAlreadyBookedTickets(Long culturalEventId) {
        // check if there is any booking
        long bookingCount = bookingRepository.count();
        // if there is any booking - count booked tickets for the cultural event,
        // if there is no booking - return 0
        return (bookingCount > 0) ? bookingRepository.countBookedTickets(culturalEventId) : 0;
    }

    private int getFreeSlots(Long culturalEventId, int maxTicketsNumber) {
        return maxTicketsNumber - getAlreadyBookedTickets(culturalEventId);
    }

    public void cancelBooking(Long id) {
        logger.info("Checking if booking with id: {} exists.", id);
        boolean bookingExists = bookingRepository.existsById(id);
        if (!bookingExists) {
            logger.error("Booking with id: {} does not exist.", id);
            throw new EntityNotFoundException(String.format("Booking with id %s does not exist.", id));
        }
        logger.info("Cancelling booking with id: {}", id);
        bookingRepository.deleteById(id);
    }
}

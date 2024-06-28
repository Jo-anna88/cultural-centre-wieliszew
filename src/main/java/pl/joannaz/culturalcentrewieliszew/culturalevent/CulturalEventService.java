package pl.joannaz.culturalcentrewieliszew.culturalevent;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.joannaz.culturalcentrewieliszew.address.AddressService;
import pl.joannaz.culturalcentrewieliszew.culturaleventbooking.BookingRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CulturalEventService {
    private final CulturalEventRepository culturalEventRepository;
    private final AddressService addressService;
    private final BookingRepository bookingRepository; // cannot use BookingService - it causes a cycle between BookingService and CulturalEventService
    private static final Logger logger = LoggerFactory.getLogger(CulturalEventService.class);
    public List<CulturalEventDTO> getAllCulturalEvents() {
        logger.info("Fetching all cultural events.");
        return culturalEventRepository.findAll().stream().map(CulturalEventDTO::new).collect(Collectors.toList());
    }

    public CulturalEventDTO getCulturalEventById(Long id) { // returns CulturalEventDTO
        logger.info("Fetching cultural event with id {}.", id);
        return new CulturalEventDTO(culturalEventRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Error during fetching cultural event with id {}", id);
                    return new EntityNotFoundException(String.format("Cultural Event with id %s not found.", id));
                }));
    }

    public CulturalEvent getCulturalEvent(Long id) { // returns CulturalEvent
        logger.info("Fetching cultural event with id {}.", id);
        return culturalEventRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Error during fetching cultural event with id {}", id);
                    return new EntityNotFoundException(String.format("Cultural Event with id %s not found.", id));
                });
    }

    public CulturalEventDTO addCulturalEvent(CulturalEventDTO culturalEventDTO) {
        logger.info("Adding new cultural event: {} to the database", culturalEventDTO.getName());
        CulturalEvent culturalEvent = new CulturalEvent(culturalEventDTO);
        culturalEvent.setAddress(addressService.getAddressById(culturalEventDTO.getLocation().getId()));
        return new CulturalEventDTO(culturalEventRepository.save(culturalEvent));
    }

    @Transactional
    public CulturalEventDTO updateCulturalEvent(CulturalEventDTO updatedCulturalEventDTO) {
        logger.info("Fetching Cultural Event: {}.", updatedCulturalEventDTO.getName());
        CulturalEvent originalCulturalEvent = culturalEventRepository.findById(updatedCulturalEventDTO.getId())
                .orElseThrow(() -> {
                    logger.error("Error during fetching Cultural Event: {}.", updatedCulturalEventDTO.getName());
                    return new EntityNotFoundException(String.format(
                                    "Cultural Event with id %s not found.", updatedCulturalEventDTO.getId()));
                    }
                );
        originalCulturalEvent.setName(updatedCulturalEventDTO.getName());
        originalCulturalEvent.setDate(updatedCulturalEventDTO.getDate());
        originalCulturalEvent.setDescription(updatedCulturalEventDTO.getDescription());
        if (!Objects.equals(originalCulturalEvent.getAddress().getId(), updatedCulturalEventDTO.getLocation().getId())) {
            originalCulturalEvent.setAddress(addressService.getAddressById(updatedCulturalEventDTO.getLocation().getId()));
        }
        logger.info("Saving updated {} Cultural Event.", updatedCulturalEventDTO.getName());
        return new CulturalEventDTO(culturalEventRepository.save(originalCulturalEvent));
    }

    @Transactional
    public Long deleteCulturalEvent(Long id) {
        logger.info("Checking if Cultural Event with id: {} exists.", id);
        boolean culturalEventExists = culturalEventRepository.existsById(id);
        if (!culturalEventExists) {
            logger.error("Cultural Event with id: {} not found.", id);
            throw new EntityNotFoundException(String.format("Cultural Event with id %s does not exist.", id));
        }

        logger.info("Deleting Cultural Event with id: {}", id);
        culturalEventRepository.deleteById(id);
        return id;
    }

    private int getMaxParticipantsNumberById(Long culturalEventId) {
        logger.info("Fetching max participant number for Cultural Event with id: {}", culturalEventId);
        return culturalEventRepository.findMaxParticipantsNumberById(culturalEventId);
    }

    public int getFreeSlots(Long culturalEventId) {
        logger.info("Counting free slots for Cultural Event with id: {}", culturalEventId);
        return (bookingRepository.count() > 0) ? // check if there is any booking
                getMaxParticipantsNumberById(culturalEventId) - bookingRepository.countBookedTickets(culturalEventId)
                : getMaxParticipantsNumberById(culturalEventId);
    }
}

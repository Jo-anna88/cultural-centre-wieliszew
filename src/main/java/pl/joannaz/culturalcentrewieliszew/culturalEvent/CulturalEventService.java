package pl.joannaz.culturalcentrewieliszew.culturalEvent;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.joannaz.culturalcentrewieliszew.address.Address;
import pl.joannaz.culturalcentrewieliszew.address.AddressRepository;
import pl.joannaz.culturalcentrewieliszew.course.Course;
import pl.joannaz.culturalcentrewieliszew.course.CourseDTO;
import pl.joannaz.culturalcentrewieliszew.course.CourseDetails;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CulturalEventService {
    private final CulturalEventRepository culturalEventRepository;
    private final AddressRepository addressRepository;
    private static final Logger logger = LoggerFactory.getLogger(CulturalEventService.class);
    public List<CulturalEventDTO> getAllCulturalEvents() {
        logger.info("Fetching all cultural events.");
        return culturalEventRepository.findAll().stream().map(CulturalEventDTO::new).collect(Collectors.toList());
    }

    public CulturalEventDTO getCulturalEventById(Long id) {
        logger.info("Fetching cultural event with id {}.", id);
        return new CulturalEventDTO(culturalEventRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Error during fetching cultural event with id {}", id);
                    return new NoSuchElementException(String.format("Cultural Event with id %s not found.", id));
                }));
    }

    public CulturalEventDTO addCulturalEvent(CulturalEventDTO culturalEventDTO) {
        logger.info("Adding new cultural event: {} to the database", culturalEventDTO.getName());
        CulturalEvent culturalEvent = new CulturalEvent(culturalEventDTO);
        culturalEvent.setAddress(getAddressById(culturalEventDTO.getLocation().getId()));
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
            originalCulturalEvent.setAddress(getAddressById(updatedCulturalEventDTO.getLocation().getId()));
        }
        logger.info("Saving updated {} Cultural Event.", updatedCulturalEventDTO.getName());
        return new CulturalEventDTO(culturalEventRepository.save(originalCulturalEvent));
    }

    @Transactional
    public Long deleteCulturalEvent(Long id) {
        logger.info("Checking if Cultural Event with id: {} exist.", id);
        boolean culturalEventExists = culturalEventRepository.existsById(id);
        if (!culturalEventExists) {
            logger.error("Cultural Event with id: {} not found.", id);
            throw new EntityNotFoundException(String.format("Cultural Event with id %s not exist.", id));
        }

        logger.info("Deleting Course Event with id: {}", id);
        culturalEventRepository.deleteById(id);
        return id;
    }

    public Address getAddressById(Integer addressId) {
        logger.info("Fetching address with id: {}", addressId);
        return addressRepository.findById(addressId)
                .orElseThrow(() -> {
                    logger.error("Error during fetching address with id: {}", addressId);
                    return new EntityNotFoundException(String.format("Location with id %s not found.", addressId));
                });
    }
}

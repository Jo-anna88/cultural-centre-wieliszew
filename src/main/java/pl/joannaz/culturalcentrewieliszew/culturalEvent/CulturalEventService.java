package pl.joannaz.culturalcentrewieliszew.culturalEvent;

import lombok.RequiredArgsConstructor;
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
    //private static final Logger log = LoggerFactory.getLogger(CulturalEventService.class);
    public List<CulturalEventDTO> getAllCourses() {
        //log.info("Fetching all courses.");
        return culturalEventRepository.findAll().stream().map(CulturalEventDTO::new).collect(Collectors.toList());
    }

    public CulturalEventDTO getCulturalEventById(Long id) {
        //log.info("Fetching cultural event with id {}.", id);
        return new CulturalEventDTO(culturalEventRepository.findById(id)
                .orElseThrow(() -> {
                    //log.error("Error during fetching cultural event with id {}", id);
                    return new NoSuchElementException(String.format("Cultural Event with id %s not found.", id));
                }));
    }

    public CulturalEventDTO addCulturalEvent(CulturalEventDTO culturalEventDTO) {
        //log.info("Saving new cultural event: {} to the database", culturalEventDTO.getName());
        CulturalEvent culturalEvent = new CulturalEvent(culturalEventDTO);
        culturalEvent.setAddress(getAddressById(culturalEventDTO.getLocation().getId()));
        return new CulturalEventDTO(culturalEventRepository.save(culturalEvent));
    }

    @Transactional
    public CulturalEventDTO updateCulturalEvent(CulturalEventDTO updatedCulturalEventDTO) {
        CulturalEvent originalCulturalEvent = culturalEventRepository.findById(updatedCulturalEventDTO.getId())
                .orElseThrow(() -> new NoSuchElementException(String.format(
                        "Cultural Event with id %s not found.", updatedCulturalEventDTO.getId()))
                );
        originalCulturalEvent.setName(updatedCulturalEventDTO.getName());
        originalCulturalEvent.setDate(updatedCulturalEventDTO.getDate());
        originalCulturalEvent.setDescription(updatedCulturalEventDTO.getDescription());
        if (!Objects.equals(originalCulturalEvent.getAddress().getId(), updatedCulturalEventDTO.getLocation().getId())) {
            originalCulturalEvent.setAddress(getAddressById(updatedCulturalEventDTO.getLocation().getId()));
        }
        return new CulturalEventDTO(culturalEventRepository.save(originalCulturalEvent));
    }

    @Transactional
    public Long deleteCulturalEvent(Long id) {
        boolean culturalEventExists = culturalEventRepository.existsById(id);
        if (!culturalEventExists) {
            throw new IllegalStateException(String.format("Cultural Event with id %s not exist.", id));
        }
        culturalEventRepository.deleteById(id);
        return id;
    }

    public Address getAddressById(Integer addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Location with id %s not found.", addressId)));
    }
}

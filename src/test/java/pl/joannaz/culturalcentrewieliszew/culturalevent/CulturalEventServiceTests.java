package pl.joannaz.culturalcentrewieliszew.culturalevent;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import pl.joannaz.culturalcentrewieliszew.address.Address;
import pl.joannaz.culturalcentrewieliszew.address.AddressService;
import pl.joannaz.culturalcentrewieliszew.address.MockAddress;
import pl.joannaz.culturalcentrewieliszew.culturaleventbooking.BookingRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CulturalEventServiceTests {
    @Mock
    private CulturalEventRepository culturalEventRepository;

    @Mock
    private AddressService addressService;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private CulturalEventService culturalEventService;

    private CulturalEvent culturalEvent;
    private List<CulturalEvent> culturalEventList;
    private CulturalEventDTO culturalEventDTO;

    @BeforeEach
    void setUp() {
        culturalEvent = MockCulturalEvent.culturalEvent;
        culturalEvent.setId(1L);
        culturalEventList = MockCulturalEvent.culturalEventList;
        culturalEventDTO = new CulturalEventDTO(culturalEvent);
    }

    @Test
    @DisplayName("getAllCulturalEvents should return all Cultural Event DTOs")
    void testGetAllCulturalEvents() {
        when(culturalEventRepository.findAll()).thenReturn(culturalEventList);

        List<CulturalEventDTO> events = culturalEventService.getAllCulturalEvents();

        assertEquals(4, events.size());
        verify(culturalEventRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getCulturalEventById should return Cultural Event DTO by id")
    void testGetCulturalEventById() {
        when(culturalEventRepository.findById(1L)).thenReturn(Optional.of(culturalEvent));

        CulturalEventDTO result = culturalEventService.getCulturalEventById(1L);

        assertEquals(culturalEvent.getName(), result.getName());
        verify(culturalEventRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getCulturalEventById should throw EntityNotFoundException against CulturalEventDTO if Cultural Event is not found")
    void testGetCulturalEventByIdNotFound() {
        when(culturalEventRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> culturalEventService.getCulturalEventById(1L));

        assertEquals("Cultural Event with id 1 not found.", exception.getMessage());
        verify(culturalEventRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getCulturalEvent should return Cultural Event")
    void testGetCulturalEvent() {
        when(culturalEventRepository.findById(1L)).thenReturn(Optional.of(culturalEvent));

        CulturalEvent result = culturalEventService.getCulturalEvent(1L);

        assertEquals(culturalEvent.getName(), result.getName());
        verify(culturalEventRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getCulturalEvent should throw EntityNotFoundException against CulturalEvent if Cultural Event is not found.")
    void testGetCulturalEventNotFound() {
        when(culturalEventRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> culturalEventService.getCulturalEvent(1L));

        assertEquals("Cultural Event with id 1 not found.", exception.getMessage());
        verify(culturalEventRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("addCulturalEvent should add new Cultural Event to the database and return CulturalEventDTO.")
    void testAddCulturalEvent() {
        int addressId = culturalEventDTO.getLocation().getId();
        Address address = MockAddress.addressList.stream()
                .filter(a -> a.getId() == addressId)
                .findFirst().get();
        when(addressService.getAddressById(addressId)).thenReturn(address);
        when(culturalEventRepository.save(any(CulturalEvent.class))).thenReturn(culturalEvent);

        CulturalEventDTO result = culturalEventService.addCulturalEvent(culturalEventDTO);

        assertEquals(culturalEvent.getName(), result.getName());
        verify(culturalEventRepository, times(1)).save(any(CulturalEvent.class));
    }

    @Test
    @DisplayName("updateCulturalEvent should save updated Cultural Event and return CulturalEventDTO")
    void testUpdateCulturalEvent() {
        CulturalEventDTO updatedCulturalEventDTO = culturalEventDTO;
        updatedCulturalEventDTO.setName("Updated Name");
        CulturalEvent updatedCulturalEvent = culturalEvent;
        updatedCulturalEvent.setName("Updated Name");
        when(culturalEventRepository.findById(1L)).thenReturn(Optional.of(culturalEvent));
        when(culturalEventRepository.save(any(CulturalEvent.class))).thenReturn(updatedCulturalEvent);

        CulturalEventDTO result = culturalEventService.updateCulturalEvent(updatedCulturalEventDTO);

        assertEquals(updatedCulturalEventDTO.getName(), result.getName());
        verify(culturalEventRepository, times(1)).findById(1L);
        verify(culturalEventRepository, times(1)).save(any(CulturalEvent.class));
    }

    @Test
    @DisplayName("updateCulturalEvent should throw EntityNotFoundException if Cultural Event is not found")
    void testUpdateCulturalEventNotFound() {
        culturalEventDTO.setId(1L);
        when(culturalEventRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> culturalEventService.updateCulturalEvent(culturalEventDTO));

        assertEquals("Cultural Event with id 1 not found.", exception.getMessage());
        verify(culturalEventRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("deleteCulturalEvent should delete Cultural Event and return id of deleted item")
    void testDeleteCulturalEvent() {
        when(culturalEventRepository.existsById(1L)).thenReturn(true);
        doNothing().when(culturalEventRepository).deleteById(1L);

        Long result = culturalEventService.deleteCulturalEvent(1L);

        assertEquals(1L, result);
        verify(culturalEventRepository, times(1)).existsById(1L);
        verify(culturalEventRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteCulturalEvent should throw EntityNotFoundException if Cultural Event is not found")
    void testDeleteCulturalEventNotFound() {
        when(culturalEventRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> culturalEventService.deleteCulturalEvent(1L));

        assertEquals("Cultural Event with id 1 does not exist.", exception.getMessage());
        verify(culturalEventRepository, times(1)).existsById(1L);
    }

    @Test
    @DisplayName("getFreeSlots should return the difference between maxParticipantsNumber and already booked tickets for cultural event by id")
    void testGetFreeSlots() {
        int maxPartNr = culturalEvent.getMaxParticipantsNumber();
        int bookedTickets = 5;
        when(bookingRepository.count()).thenReturn(1L);
        when(bookingRepository.countBookedTickets(1L)).thenReturn(bookedTickets);
        when(culturalEventRepository.findMaxParticipantsNumberById(1L)).thenReturn(maxPartNr);

        int result = culturalEventService.getFreeSlots(1L);

        assertEquals(maxPartNr - bookedTickets, result);
        verify(bookingRepository, times(1)).count();
        verify(bookingRepository, times(1)).countBookedTickets(1L);
        verify(culturalEventRepository, times(1)).findMaxParticipantsNumberById(1L);
    }

    @Test
    @DisplayName("getFreeSlots should return maxParticipantsNumber for cultural event by id if there is no booking")
    void testGetFreeSlotsNoBookings() {
        when(bookingRepository.count()).thenReturn(0L);
        when(culturalEventRepository.findMaxParticipantsNumberById(1L)).thenReturn(culturalEvent.getMaxParticipantsNumber());

        int result = culturalEventService.getFreeSlots(1L);

        assertEquals(culturalEvent.getMaxParticipantsNumber(), result);
        verify(bookingRepository, times(1)).count();
        verify(culturalEventRepository, times(1)).findMaxParticipantsNumberById(1L);
    }
}

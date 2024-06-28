package pl.joannaz.culturalcentrewieliszew.culturalevent;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path="/api/events")
public class CulturalEventController {
    private final CulturalEventService culturalEventService;

    @GetMapping
    public ResponseEntity<List<CulturalEventDTO>> getAllCulturalEvents(HttpServletResponse response) throws InterruptedException {
        //Thread.sleep(3000); // to check dealing with slow REST responses
        //List<CulturalEventDTO> culturalEvents = culturalEventService.getAllCulturalEvents();
        //return ResponseEntity.ok(culturalEvents);
        response.setStatus(500);
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CulturalEventDTO> getCulturalEventById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(culturalEventService.getCulturalEventById(id));
    }

    @PostMapping()
    public CulturalEventDTO addCulturalEvent(@RequestBody CulturalEventDTO culturalEventDTO) {
        return culturalEventService.addCulturalEvent(culturalEventDTO); // id will be automatically added
    }

    @PutMapping()
    public CulturalEventDTO updateCulturalEvent(@RequestBody CulturalEventDTO updatedCulturalEventDTO) {
        return culturalEventService.updateCulturalEvent(updatedCulturalEventDTO);
    }

    @DeleteMapping("/{id}")
    public Long deleteCulturalEvent(@PathVariable("id") Long id) {
        return culturalEventService.deleteCulturalEvent(id);
    }

    @GetMapping("/{id}/free-slots")
    public Integer getFreeSlotsById(@PathVariable("id") Long culturalEventId) {
        return culturalEventService.getFreeSlots(culturalEventId);
    }
}

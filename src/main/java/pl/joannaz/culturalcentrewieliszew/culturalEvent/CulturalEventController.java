package pl.joannaz.culturalcentrewieliszew.culturalEvent;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path="/api/events")
public class CulturalEventController {
    private final CulturalEventService culturalEventService;

    @GetMapping
    public List<CulturalEventDTO> getAllCourses(HttpServletResponse response) throws InterruptedException {
        //Thread.sleep(3000); // to check dealing with slow REST responses
        return culturalEventService.getAllCourses();
        //response.setStatus(500);
        //return null;
    }

    @GetMapping("/{id}")
    public CulturalEventDTO getCulturalEventById(@PathVariable("id") Long id) {
        return culturalEventService.getCulturalEventById(id);
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
}

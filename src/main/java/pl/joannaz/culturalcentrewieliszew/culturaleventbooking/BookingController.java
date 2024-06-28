package pl.joannaz.culturalcentrewieliszew.culturaleventbooking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.joannaz.culturalcentrewieliszew.user.User;
import pl.joannaz.culturalcentrewieliszew.user.UserService;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping(path="/api/booking")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;
    @GetMapping()
    public List<BookingDTO> getBookingsForCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return bookingService.getBookingsByUserId(currentUser.getId());
    }

    @PostMapping()
    public BookingDTO bookTicket(@RequestBody BookingDTO bookingDTO) {
        return bookingService.bookTicket(bookingDTO);
    }

    @DeleteMapping("/{id}")
    public void cancelBooking(@PathVariable("id") Long id) {
        bookingService.cancelBooking(id);
    }
}

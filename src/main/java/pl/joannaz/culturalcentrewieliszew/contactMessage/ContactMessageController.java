package pl.joannaz.culturalcentrewieliszew.contactMessage;

import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/api/contact")
public class ContactMessageController {
    private final ContactMessageService contactMessageService;

    public ContactMessageController(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
    }

    @PostMapping()
    public void sendMessage(@RequestBody ContactMessage message) {
        try {
            this.contactMessageService.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

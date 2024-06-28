package pl.joannaz.culturalcentrewieliszew.contactmessage;

import lombok.Data;

@Data
public class ContactMessage {
    String name;
    String email;
    String subject;
    String message;
}

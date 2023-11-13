package pl.joannaz.culturalcentrewieliszew.contactMessage;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class ContactMessageService {
    private final JavaMailSender sender;
    @Value("${spring.mail.username}")
    private String recipient;

    public ContactMessageService (JavaMailSender javaMailSender) {
        this.sender = javaMailSender;
    }

    public void send (ContactMessage message) throws MessagingException {
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(recipient);
            helper.setSubject(message.subject);
            helper.setText("Name: " + message.name + "\nEmail: " + message.email + "\nText: " + message.message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        try {
            sender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

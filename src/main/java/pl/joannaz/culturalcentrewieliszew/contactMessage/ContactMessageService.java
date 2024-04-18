package pl.joannaz.culturalcentrewieliszew.contactMessage;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class ContactMessageService {
    private final JavaMailSender sender;
    @Value("${spring.mail.username}")
    private String recipient;
    private static final Logger logger = LoggerFactory.getLogger(ContactMessageService.class);

    public ContactMessageService (JavaMailSender javaMailSender) {
        this.sender = javaMailSender;
    }

    public void send (ContactMessage message) throws MessagingException {
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            logger.info("Creating MimeMessage");
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(recipient);
            helper.setSubject(message.subject);
            helper.setText("Name: " + message.name + "\nEmail: " + message.email + "\nText: " + message.message);

        } catch (MessagingException e) {
            logger.error("Error during creating MimeMessage: {}", e.getMessage());
        }

        try {
            logger.info("Sending message with usage of JavaMailSender.");
            sender.send(mimeMessage);
        } catch (Exception e) {
            logger.error("Error during sending message: {}", e.getMessage());
        }
    }

}

package com.sadetech.Actuator.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${spring.mail.to}")
    private String recipient;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipient);
            message.setSubject(subject);
            message.setText(body);
            javaMailSender.send(message);
            logger.info("Email sent successfully to user {}",recipient);
        } catch (Exception e) {
            logger.error("Failed to send email: {}", e.getMessage());
        }
    }
}


package com.sadetech.admin_login.Service;

import com.sadetech.admin_login.Repository.OtpRepository;
import com.sadetech.admin_login.model.OtpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {

    @Autowired
    private OtpRepository otpRepository;

    private final JavaMailSender javaMailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendOtpEmail(String to, String otp, String otpType) {
        if (to == null || otp == null || otpType == null) {
            throw new IllegalArgumentException("Recipient email, OTP, and OTP type must not be null.");
        }

        String subject;
        String text = switch (otpType) {
            case "Sign up" -> {
                subject = "Your OTP Code for Registration";
                yield "Your OTP code for registration is: " + otp;
            }
            case "Reset-Password" -> {
                subject = "Your OTP Code for Reset Password";
                yield "Your OTP code for resetting your password is: " + otp;
            }
            default -> throw new IllegalArgumentException("Unsupported OTP type: " + otpType);
        };

        // Save email content to the database
        OtpEntity otpEntity = new OtpEntity(to, otp, otpType, text, LocalDateTime.now());
        otpRepository.save(otpEntity);

        // Send the email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            logger.info("Attempting to send email to: {}", to);
            javaMailSender.send(message);
            logger.info("Email sent successfully to: {}", to);
        } catch (MailException e) {
            logger.error("Failed to send email to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

}

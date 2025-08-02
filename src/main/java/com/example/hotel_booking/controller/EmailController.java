package com.example.hotel_booking.controller;

import com.example.hotel_booking.service.impl.EmailService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailController {

    EmailService emailService;

    @GetMapping("/test")
    public ResponseEntity<String> testSendEmail() {
        try {
            emailService.sendBookingConfirmation(
                    "nguyenvietvan223@gmail.com",
                    "Nguyễn Văn A",
                    "ABC123XYZ",
                    "https://your-domain.com/confirm?code=ABC123XYZ"
            );
            return ResponseEntity.ok("Email sent successfully!");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending email: " + e.getMessage());
        }
    }
}

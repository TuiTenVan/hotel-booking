package com.example.hotel_booking.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    JavaMailSender mailSender;
    SpringTemplateEngine templateEngine;

    public void sendBookingConfirmation(String toEmail, String userName, String confirmCode, String confirmLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("confirmCode", confirmCode);
        context.setVariable("confirmLink", confirmLink);

        String htmlContent = templateEngine.process("booking-confirmation.html", context);

        helper.setTo(toEmail);
        helper.setSubject("Xác nhận đặt phòng");
        helper.setText(htmlContent, true);
        helper.setFrom("nguyenvietvan223@gmail.com");
        log.info("Sending booking confirmation email to: {}", toEmail);

        mailSender.send(message);
    }
}

package com.example.hotel_booking.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

//public class EmailService {
//    JavaMailSender mailSender;
//    SpringTemplateEngine templateEngine;
//
//    public void sendBookingConfirmation(String toEmail, String userName, String confirmCode, String confirmLink)
//            throws MessagingException, IOException, WriterException {
//
//        // 1. Tạo QR code từ confirmLink
//        ByteArrayOutputStream qrOutputStream = new ByteArrayOutputStream();
//        BitMatrix bitMatrix = new QRCodeWriter().encode(confirmLink, BarcodeFormat.QR_CODE, 250, 250);
//        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", qrOutputStream);
//
//        byte[] qrBytes = qrOutputStream.toByteArray();
//        ByteArrayResource qrImage = new ByteArrayResource(qrBytes);
//
//        // 2. Cấu hình email
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//        // 3. Truyền biến vào template Thymeleaf
//        Context context = new Context();
//        context.setVariable("userName", userName);
//        context.setVariable("confirmCode", confirmCode);
//        context.setVariable("confirmLink", confirmLink);
//
//        String htmlContent = templateEngine.process("booking-confirmation.html", context);
//
//        helper.setTo(toEmail);
//        helper.setSubject("Xác nhận đặt phòng");
//        helper.setText(htmlContent, true);
//        helper.setFrom("nguyenvietvan223@gmail.com");
//
//        // 4. Gắn ảnh QR vào email nội tuyến (dùng cid:qrImage trong HTML)
//        helper.addInline("qrImage", qrImage, "image/png");
//
//        log.info("Sending booking confirmation email to: {}", toEmail);
//        mailSender.send(message);
//    }
//}

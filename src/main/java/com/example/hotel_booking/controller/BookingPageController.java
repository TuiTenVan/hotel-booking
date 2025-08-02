package com.example.hotel_booking.controller;

import com.example.hotel_booking.entity.BookedRoom;
import com.example.hotel_booking.exception.ResourceNotFoundException;
import com.example.hotel_booking.service.IBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingPageController {

    private final IBookingService iBookingService;

    @GetMapping("/confirm/{confirmCode}")
    public String getBookingConfirmCode(@PathVariable String confirmCode, Model model) {
        try {
            BookedRoom bookedRoom = iBookingService.findByConfirmCode(confirmCode);

            model.addAttribute("guestName", bookedRoom.getGuestFullName());
            model.addAttribute("confirmCode", confirmCode);

            return "booking-success";
        } catch (ResourceNotFoundException e) {
            return "booking-failed";
        }
    }
}

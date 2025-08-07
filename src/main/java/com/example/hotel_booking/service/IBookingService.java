package com.example.hotel_booking.service;

import com.example.hotel_booking.entity.BookedRoom;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IBookingService {
    void cancelBooking(Long bookingId);
    String saveBooking(Long roomId, BookedRoom bookedReq);
    BookedRoom findByConfirmCode(String confirmCode);
    List<BookedRoom> getAllBookings();
    List<BookedRoom> getBookingsByUserEmail(String email);
    List<BookedRoom> getAllBookingByRoomId(Long roomId);
    BookedRoom findById(Long id);
}

package com.example.hotel_booking.service.impl;

import com.example.hotel_booking.entity.BookedRoom;
import com.example.hotel_booking.enums.BookingStatus;
import com.example.hotel_booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class BookingStatusScheduler {
    BookingRepository bookingRepository;

    @Scheduled(fixedRate = 60000)
    public void markExpiredBookingsAsFailed() {
        LocalDateTime now = LocalDateTime.now();
        List<BookedRoom> pendingBookings = bookingRepository.findAllByStatus(BookingStatus.PENDING);

        for (BookedRoom booking : pendingBookings) {
            if (booking.getCreatedAt().plusMinutes(1).isBefore(now)) {
                booking.setStatus(BookingStatus.FAILED);
                bookingRepository.save(booking);
            }
        }
    }
}

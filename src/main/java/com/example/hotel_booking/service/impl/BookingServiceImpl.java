package com.example.hotel_booking.service.impl;

import com.example.hotel_booking.entity.BookedRoom;
import com.example.hotel_booking.entity.Room;
import com.example.hotel_booking.enums.BookingStatus;
import com.example.hotel_booking.exception.InvalidBookingRequestException;
import com.example.hotel_booking.exception.ResourceNotFoundException;
import com.example.hotel_booking.repository.BookingRepository;
import com.example.hotel_booking.service.IBookingService;
import com.example.hotel_booking.service.IRoomService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements IBookingService {
    BookingRepository bookingRepository;
    IRoomService iRoomService;
    EmailService emailService;

    @Override
    public List<BookedRoom> getAllBookingByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        BookedRoom booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        log.info("Cancelling booking with ID: {}", bookingId);
        booking.setActive(0);
        bookingRepository.save(booking);
    }


    @Override
    public String saveBooking(Long roomId, BookedRoom bookedReq) {
        if (bookedReq.getCheckOut().isBefore(bookedReq.getCheckIn())) {
            throw new InvalidBookingRequestException("Check-in date must be before check-out date!");
        }
        Room room = iRoomService.getRoomById(roomId).get();

        if(bookedReq.getTotalNumOfGuest() > room.getCapacity()) {
            throw new InvalidBookingRequestException("Total number of guests exceeds room capacity!");
        }

        List<BookedRoom> bookedRooms = room.getBookedRooms();
        boolean roomIsAvailable = roomIsAvailable(bookedReq, bookedRooms);
        if (!roomIsAvailable) {
            throw new InvalidBookingRequestException("This room is not available for the selected dates");
        }

        bookedReq.setStatus(BookingStatus.PENDING);

        room.addBooking(bookedReq);

        bookingRepository.save(bookedReq);

        String confirmCode = bookedReq.getConfirmCode();
        String confirmLink = "http://localhost:8088/api/bookings/confirm/" + confirmCode;

        try {
            emailService.sendBookingConfirmation(
                    bookedReq.getGuestEmail(),
                    bookedReq.getGuestFullName(),
                    confirmCode,
                    confirmLink
            );
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send confirmation email", e);
        }

        return confirmCode;
    }


    private boolean roomIsAvailable(BookedRoom bookedReq, List<BookedRoom> bookedRooms) {
        return bookedRooms.stream().noneMatch(b -> bookedReq.getCheckIn().equals(b.getCheckIn())
                || bookedReq.getCheckOut().isBefore(b.getCheckOut())
                || (bookedReq.getCheckIn().isAfter(b.getCheckIn()) && bookedReq.getCheckIn().isBefore(b.getCheckOut()))
                || (bookedReq.getCheckIn().isBefore(b.getCheckIn())

                && bookedReq.getCheckOut().equals(b.getCheckOut()))
                || (bookedReq.getCheckIn().isBefore(b.getCheckIn())

                && bookedReq.getCheckOut().isAfter(b.getCheckOut()))

                || (bookedReq.getCheckIn().equals(b.getCheckOut()) && bookedReq.getCheckOut().equals(b.getCheckIn()))

                || (bookedReq.getCheckIn().equals(b.getCheckOut())
                && bookedReq.getCheckOut().equals(bookedReq.getCheckIn())));

    }

    @Override
    @Transactional
    public BookedRoom findByConfirmCode(String confirmCode) {
        BookedRoom bookedRoom = bookingRepository.findByConfirmCode(confirmCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking not found with confirm code: " + confirmCode));

        if (bookedRoom.getStatus() == BookingStatus.PENDING) {
            bookedRoom.setStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(bookedRoom);
        }
        return bookedRoom;
    }


    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAllByActiveOrderByCheckInDesc(1);
    }

    @Override
    public List<BookedRoom> getBookingsByUserEmail(String email) {
        return bookingRepository.findByGuestEmail(email);
    }

}

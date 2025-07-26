package com.example.hotel_booking.service.impl;

import com.example.hotel_booking.entity.BookedRoom;
import com.example.hotel_booking.entity.Room;
import com.example.hotel_booking.exception.InvalidBookingRequestException;
import com.example.hotel_booking.exception.ResourceNotFoundException;
import com.example.hotel_booking.repository.BookingRepository;
import com.example.hotel_booking.service.IBookingService;
import com.example.hotel_booking.service.IRoomService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements IBookingService {

    BookingRepository bookingRepository;
    IRoomService iRoomService;

    @Override
    public List<BookedRoom> getAllBookingByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);

    }

    @Override
    public String saveBooking(Long roomId, BookedRoom bookedReq) {
        if (bookedReq.getCheckOut().isBefore(bookedReq.getCheckIn())) {
            throw new InvalidBookingRequestException("Check-in date must be before check-out date!");
        }
        Room room = iRoomService.getRoomById(roomId).get();
        List<BookedRoom> bookedRooms = room.getBookedRooms();
        boolean roomIsAvailable = roomIsAvailable(bookedReq, bookedRooms);
        if (roomIsAvailable) {
            room.addBooking(bookedReq);
            bookingRepository.save(bookedReq);
        } else {
            throw new InvalidBookingRequestException("This room is not available for the selected dates");
        }
        return bookedReq.getConfirmCode();
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
    public BookedRoom findByConfirmCode(String confirmCode) {

        return bookingRepository.findByConfirmCode(confirmCode)
                .orElseThrow(() -> new ResourceNotFoundException("No booking found with booking code: " + confirmCode));
    }

    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<BookedRoom> getBookingsByUserEmail(String email) {
        return bookingRepository.findByGuestEmail(email);
    }

}

package com.example.hotel_booking.controller;

import com.example.hotel_booking.dto.response.BookingResponse;
import com.example.hotel_booking.dto.response.RoomResponse;
import com.example.hotel_booking.entity.BookedRoom;
import com.example.hotel_booking.entity.Room;
import com.example.hotel_booking.exception.InvalidBookingRequestException;
import com.example.hotel_booking.exception.ResourceNotFoundException;
import com.example.hotel_booking.service.IBookingService;
import com.example.hotel_booking.service.IRoomService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {
    IBookingService iBookingService;
    IRoomService iRoomService;

    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookingRooms() {
        List<BookedRoom> bookedRooms = iBookingService.getAllBookings();

        List<BookingResponse> bookingResponses = bookedRooms.stream()
                .map(this::getBookingResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(bookingResponses);
    }


    @GetMapping("/confirm/{confirmCode}")
    public ResponseEntity<?> getBookingConfirmCode(@PathVariable String confirmCode) {
        try {
            BookedRoom bookedRoom = iBookingService.findByConfirmCode(confirmCode);
            BookingResponse bookingResponse = getBookingResponse(bookedRoom);

            return ResponseEntity.ok(bookingResponse);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    private BookingResponse getBookingResponse(BookedRoom bookedRoom) {
        Room theRoom = iRoomService.getRoomById(bookedRoom.getRoom().getId()).get();
        RoomResponse roomResponse = new RoomResponse(theRoom.getId(), theRoom.getRoomType().name(), theRoom.getRoomPrice());

        return new BookingResponse(bookedRoom.getId(), bookedRoom.getCheckIn(),
                bookedRoom.getCheckOut(),
                bookedRoom.getGuestFullName(), bookedRoom.getGuestEmail(), bookedRoom.getNumOfAdults(),
                bookedRoom.getNumOfChildren(), bookedRoom.getTotalNumOfGuest(), bookedRoom.getConfirmCode(), roomResponse);
    }

    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long roomId, @RequestBody BookedRoom bookedReq) {
        try {
            String code = iBookingService.saveBooking(roomId, bookedReq);
            return ResponseEntity.ok("Confirmation for Booking ID#" + code + " Check-in " + bookedReq.getCheckIn());
        } catch (InvalidBookingRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{email}/bookings")
    public ResponseEntity<List<BookingResponse>> getBookingsByUserEmail(@PathVariable String email) {
        List<BookedRoom> bookings = iBookingService.getBookingsByUserEmail(email);

        List<BookingResponse> bookingResponses = bookings.stream()
                .map(this::getBookingResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(bookingResponses);
    }


    @DeleteMapping("/{bookingId}/delete")
    public void cancelBooking(@PathVariable Long bookingId) {
        iBookingService.cancelBooking(bookingId);
    }
}

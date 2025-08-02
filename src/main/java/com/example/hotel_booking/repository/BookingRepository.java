package com.example.hotel_booking.repository;

import java.util.List;
import java.util.Optional;

import com.example.hotel_booking.entity.BookedRoom;
import com.example.hotel_booking.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<BookedRoom, Long> {
    Optional<BookedRoom> findByConfirmCode(String confirmCode);
    List<BookedRoom> findByRoomId(Long roomId);
    List<BookedRoom> findByGuestEmail(String email);
    List<BookedRoom> findAllByActiveOrderByCheckInDesc(Integer status);
    List<BookedRoom> findAllByStatus(BookingStatus status);
}

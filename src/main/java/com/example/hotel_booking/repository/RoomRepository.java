package com.example.hotel_booking.repository;


import java.time.LocalDate;
import java.util.List;

import com.example.hotel_booking.enums.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.hotel_booking.entity.Room;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Long> {

	@Query("SELECT DISTINCT r.roomType FROM Room r")
	List<String> findRoomTypes();

	@Query("""
    SELECT r FROM Room r 
    WHERE (:roomType IS NULL OR r.roomType = :roomType)
    AND r.id NOT IN (
        SELECT br.room.id FROM BookedRoom br 
        WHERE ((br.checkIn <= :checkOut) AND (br.checkOut >= :checkIn))
    )
""")
	List<Room> findAvailableRooms(
			@Param("checkIn") LocalDate checkIn,
			@Param("checkOut") LocalDate checkOut,
			@Param("roomType") RoomType roomType
	);


}

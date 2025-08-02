package com.example.hotel_booking.dto.response;

import java.time.LocalDate;

import com.example.hotel_booking.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
	private Long id;
	private LocalDate checkIn;
	private LocalDate checkOut;
	private String guestFullName;
	private String guestEmail;
	private int numOfChildren;
	private int numOfAdults;
	private int totalNumOfGuest;
	private String bookingCode;
	private RoomResponse room;
	private String status;

	public BookingResponse(Long id, LocalDate checkIn, LocalDate checkOut, String bookingCode, BookingStatus status) {
		super();
		this.id = id;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
		this.bookingCode = bookingCode;
		this.status = status.name();
	}
}

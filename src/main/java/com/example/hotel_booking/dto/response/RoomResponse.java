package com.example.hotel_booking.dto.response;

import java.math.BigDecimal;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;

import lombok.Data;

@Data
public class RoomResponse {
	private Long id;
	private String roomType;
	private BigDecimal roomPrice;
	private boolean isBooked;
	private String image;
	private List<BookingResponse> bookings;
	private String roomNumber;
	private Integer capacity;

	public RoomResponse(Long id, String roomType, BigDecimal roomPrice) {
		super();
		this.id = id;
		this.roomType = roomType;
		this.roomPrice = roomPrice;
	}

	@SuppressWarnings("deprecation")
	public RoomResponse(Long id, String roomType, BigDecimal roomPrice, boolean isBooked, byte[] imageBytes, List<BookingResponse> bookings, String roomNumber, Integer capacity) {
		super();
		this.id = id;
		this.roomType = roomType;
		this.roomPrice = roomPrice;
		this.isBooked = isBooked;
		this.image = imageBytes != null ? Base64.encodeBase64String(imageBytes) : null;
		this.bookings = bookings;
		this.roomNumber = roomNumber;
		this.capacity = capacity;
	}
}
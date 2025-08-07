package com.example.hotel_booking.dto.response;

import lombok.Data;
import org.apache.tomcat.util.codec.binary.Base64;

import java.math.BigDecimal;
import java.util.List;

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
	private Integer quantity;
	private String description;
	private List<ExtraServiceResponse> services;


	public RoomResponse(Long id, String roomType, BigDecimal roomPrice) {
		super();
		this.id = id;
		this.roomType = roomType;
		this.roomPrice = roomPrice;
	}

	@SuppressWarnings("deprecation")
	public RoomResponse(Long id, String roomType, BigDecimal roomPrice, boolean isBooked,
						byte[] imageBytes, List<BookingResponse> bookings, String roomNumber,
						Integer capacity, Integer quantity, String description, List<ExtraServiceResponse> services) {
		super();
		this.id = id;
		this.roomType = roomType;
		this.roomPrice = roomPrice;
		this.isBooked = isBooked;
		this.image = imageBytes != null ? Base64.encodeBase64String(imageBytes) : null;
		this.bookings = bookings;
		this.roomNumber = roomNumber;
		this.capacity = capacity;
		this.quantity = quantity;
		this.description = description;
		this.services = services;
	}
}
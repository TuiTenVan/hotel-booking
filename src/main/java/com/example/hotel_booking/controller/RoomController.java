package com.example.hotel_booking.controller;

import com.example.hotel_booking.dto.request.RoomRequest;
import com.example.hotel_booking.dto.response.BookingResponse;
import com.example.hotel_booking.dto.response.ExtraServiceResponse;
import com.example.hotel_booking.dto.response.RoomResponse;
import com.example.hotel_booking.entity.BookedRoom;
import com.example.hotel_booking.entity.Room;
import com.example.hotel_booking.enums.RoomType;
import com.example.hotel_booking.exception.ImageRetrievalException;
import com.example.hotel_booking.exception.ResourceNotFoundException;
import com.example.hotel_booking.service.IBookingService;
import com.example.hotel_booking.service.IRoomService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomController {
	IRoomService roomService;
    IBookingService bookingService;

	@PostMapping("/addNewRoom")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<RoomResponse> addNewRoom(@RequestPart("image") MultipartFile image,
												   @RequestPart("room") RoomRequest roomRequest
	) {
		try {
			Room savedRoom = roomService.addNewRoom(image, roomRequest);
			RoomResponse response = new RoomResponse(
					savedRoom.getId(),
					savedRoom.getRoomType().name(),
					savedRoom.getRoomPrice()
			);
			return ResponseEntity.ok(response);
		} catch (SQLException | IOException e) {
			return ResponseEntity.status(500).build();
		}
	}


	@GetMapping("/roomTypes")
	public ResponseEntity<List<String>> getRoomTypes() {
		List<String> types = Arrays.stream(RoomType.values())
				.map(Enum::name)
				.collect(Collectors.toList());
		return ResponseEntity.ok(types);
	}

	@GetMapping("/allRooms")
	public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
		List<Room> listRooms = roomService.getAllRooms();
		List<RoomResponse> roomResponses = new ArrayList<>();
		for (Room room : listRooms) {
			byte[] imageBytes = roomService.getRoomImageById(room.getId());
			if (imageBytes != null && imageBytes.length > 0) {
				String base64Image = Base64.encodeBase64String(imageBytes);
				RoomResponse response = getRoomResponse(room);
				response.setImage(base64Image);
				roomResponses.add(response);
			}
		}
		return ResponseEntity.ok(roomResponses);
	}

	@DeleteMapping("/delete/room/{roomId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
		roomService.deleteRoom(roomId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/update/{roomId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,
												   @RequestPart(name = "image", required = false) MultipartFile image,
												   @RequestPart("room") RoomRequest roomRequest
	) {
		try {
			Room updatedRoom = roomService.updateRoom(roomId, image, roomRequest);
			RoomResponse response = new RoomResponse(
					updatedRoom.getId(),
					updatedRoom.getRoomType().name(),
					updatedRoom.getRoomPrice()
			);
			return ResponseEntity.ok(response);
		} catch (IOException | SQLException e) {
			return ResponseEntity.status(500).build();
		}
	}

	@GetMapping("/room/{roomId}")
	public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId) {
		Optional<Room> roomOptional = roomService.getRoomById(roomId);
		return roomOptional.map(room -> {
			RoomResponse response = getRoomResponse(room);
			return ResponseEntity.ok(Optional.of(response));
		}).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
	}

	@GetMapping("/available-rooms")
	public ResponseEntity<List<RoomResponse>> getRoomAvailable(
			@RequestParam("checkIn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
			@RequestParam("checkOut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
			@RequestParam("roomType") RoomType roomType) {
		List<Room> availableRoom = roomService.getAvailableRooms(checkIn, checkOut, roomType);

		List<RoomResponse> roomResponses = availableRoom.stream()
				.map(room -> {
					byte[] photoBytes = roomService.getRoomImageById(room.getId());
					if (photoBytes != null && photoBytes.length > 0) {
						String imageBase64 = Base64.encodeBase64String(photoBytes);
						RoomResponse roomResponse = getRoomResponse(room);
						roomResponse.setImage(imageBase64);
						return roomResponse;
					}
					return null;
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toList());

		if (roomResponses.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(roomResponses);
		}
	}

	private RoomResponse getRoomResponse(Room room) {
		List<BookedRoom> bookedRooms = getAllBookingByRoomId(room.getId());
		List<BookingResponse> bookingResponses = bookedRooms.stream()
				.map(booking -> new BookingResponse(booking.getId(), booking.getCheckIn(), booking.getCheckOut(),
						booking.getConfirmCode(), booking.getStatus(), booking.getCreatedAt()))
				.toList();
		byte[] imageBytes = null;
		Blob blobImage = room.getImage();
		if (blobImage != null) {
			try {
				imageBytes = blobImage.getBytes(1, (int) blobImage.length());
			} catch (SQLException e) {
				throw new ImageRetrievalException("Error get image response");
			}
		}
		List<ExtraServiceResponse> serviceResponses = room.getServices().stream().map(service -> {
			ExtraServiceResponse dto = new ExtraServiceResponse();
			dto.setId(service.getId());
			dto.setServiceName(service.getServiceName());
			dto.setDescription(service.getDescription());
			dto.setPrice(service.getPrice());
			return dto;
		}).toList();

		return new RoomResponse(room.getId(), room.getRoomType().name(), room.getRoomPrice(), room.isBooked(), imageBytes,
				bookingResponses, room.getRoomNumber(), room.getCapacity(), room.getQuantity(), room.getDescription(), serviceResponses);
	}

	private List<BookedRoom> getAllBookingByRoomId(Long roomId) {
		return bookingService.getAllBookingByRoomId(roomId);
	}
}


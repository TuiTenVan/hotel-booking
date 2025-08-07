package com.example.hotel_booking.service.impl;

import com.example.hotel_booking.dto.request.RoomRequest;
import com.example.hotel_booking.entity.ExtraService;
import com.example.hotel_booking.entity.Room;
import com.example.hotel_booking.enums.RoomType;
import com.example.hotel_booking.exception.ResourceNotFoundException;
import com.example.hotel_booking.repository.ExtraServiceRepository;
import com.example.hotel_booking.repository.RoomRepository;
import com.example.hotel_booking.service.IRoomService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RoomServiceImpl implements IRoomService {
	ExtraServiceRepository extraServiceRepository;
	RoomRepository roomRepository;

	@Override
	public Room addNewRoom(MultipartFile imageFile, RoomRequest roomRequest) throws SQLException, IOException {

		Room room = new Room();

		room.setRoomType(RoomType.valueOf(roomRequest.getRoomType()));
		room.setRoomPrice(roomRequest.getRoomPrice());
		room.setRoomName(roomRequest.getRoomName());
		room.setDescription(roomRequest.getDescription());
		room.setRoomNumber(roomRequest.getRoomNumber());
		room.setQuantity(roomRequest.getQuantity());
		room.setCapacity(roomRequest.getCapacity());
		room.setActive(roomRequest.getActive() != null ? roomRequest.getActive() : 1); // Default to 1

		if (roomRequest.getServiceIds() != null && !roomRequest.getServiceIds().isEmpty()) {
			List<ExtraService> services = extraServiceRepository.findAllById(roomRequest.getServiceIds());
			room.setServices(services);
		}
		if (!imageFile.isEmpty()) {
			byte[] imageBytes = imageFile.getBytes();
			Blob imageBlob = new SerialBlob(imageBytes);
			room.setImage(imageBlob);
		}
		return roomRepository.save(room);
	}

	@Override
	public List<Room> getAllRooms() {
		return roomRepository.findAllByActive(1);
	}


	@Override
	public byte[] getRoomImageById(Long roomId) {
		Optional<Room> optional = roomRepository.findById(roomId);
		if (optional.isEmpty()) {
			throw new ResourceNotFoundException("Room not found with ID " + roomId);
		}
		Blob imageBlob = optional.get().getImage();
		if (imageBlob != null) {
			try {
				return imageBlob.getBytes(1, (int) imageBlob.length());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void deleteRoom(Long roomId) {
		Room room = roomRepository.findById(roomId)
				.orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));

		room.setActive(0);
		roomRepository.save(room);
	}

	@Override
	public Room updateRoom(Long roomId, MultipartFile imageFile, RoomRequest roomRequest) throws IOException, SQLException {
		Room room = roomRepository.findById(roomId)
				.orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));

		if (roomRequest.getRoomType() != null) {
			room.setRoomType(RoomType.valueOf(roomRequest.getRoomType()));
		}

		if (roomRequest.getRoomPrice() != null) {
			room.setRoomPrice(roomRequest.getRoomPrice());
		}

		if (roomRequest.getRoomName() != null) {
			room.setRoomName(roomRequest.getRoomName());
		}

		if (roomRequest.getDescription() != null) {
			room.setDescription(roomRequest.getDescription());
		}

		if (roomRequest.getRoomNumber() != null) {
			room.setRoomNumber(roomRequest.getRoomNumber());
		}

		if (roomRequest.getQuantity() != null) {
			room.setQuantity(roomRequest.getQuantity());
		}

		if (roomRequest.getCapacity() != null) {
			room.setCapacity(roomRequest.getCapacity());
		}

		if (roomRequest.getActive() != null) {
			room.setActive(roomRequest.getActive());
		}

		if (roomRequest.getServiceIds() != null && !roomRequest.getServiceIds().isEmpty()) {
			List<ExtraService> services = extraServiceRepository.findAllById(roomRequest.getServiceIds());
			room.setServices(services);
		}

		if (imageFile != null && !imageFile.isEmpty()) {
			byte[] imageBytes = imageFile.getBytes();
			Blob imageBlob = new SerialBlob(imageBytes);
			room.setImage(imageBlob);
		}

		return roomRepository.save(room);
	}



	@Override
	public Optional<Room> getRoomById(Long roomId) {
		return Optional.of(roomRepository.findById(roomId).get());
	}

	@Override
	public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut, RoomType roomType) {
		return roomRepository.findAvailableRooms(checkIn, checkOut, roomType);
	}
}

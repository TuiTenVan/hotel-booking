package com.example.hotel_booking.service.impl;

import com.example.hotel_booking.entity.ExtraService;
import com.example.hotel_booking.entity.Room;
import com.example.hotel_booking.exception.ResourceNotFoundException;
import com.example.hotel_booking.repository.ExtraServiceRepository;
import com.example.hotel_booking.repository.RoomRepository;
import com.example.hotel_booking.service.IExtraService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExtraServiceImpl implements IExtraService {
    ExtraServiceRepository extraServiceRepository;
    RoomRepository roomRepository;

    @Override
    public List<ExtraService> getExtraServices() {
        return extraServiceRepository.findAll();
    }

    @Override
    public ExtraService getExtraServiceById(Long id) {
        return extraServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Extra service not found with id: " + id));
    }

    @Override
    public ExtraService createExtraService(ExtraService extraService) {
        return extraServiceRepository.save(extraService);
    }

    @Override
    public ExtraService updateExtraService(Long id, ExtraService updatedService) {
        ExtraService existing = getExtraServiceById(id);
        existing.setServiceName(updatedService.getServiceName());
        existing.setDescription(updatedService.getDescription());
        existing.setPrice(updatedService.getPrice());
        return extraServiceRepository.save(existing);
    }

    @Override
    public void deleteExtraService(Long id) {
        ExtraService existing = getExtraServiceById(id);
        extraServiceRepository.delete(existing);
    }

    @Override
    public List<ExtraService> getExtraServicesByRoomId(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));
        return room.getServices(); // assuming the field is named "services"
    }
}

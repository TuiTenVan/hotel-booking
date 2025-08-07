package com.example.hotel_booking.service;

import com.example.hotel_booking.entity.ExtraService;

import java.util.List;

public interface IExtraService {
    List<ExtraService> getExtraServices();
    ExtraService getExtraServiceById(Long id);
    ExtraService createExtraService(ExtraService extraService);
    ExtraService updateExtraService(Long id, ExtraService extraService);
    void deleteExtraService(Long id);
    List<ExtraService> getExtraServicesByRoomId(Long roomId);
}

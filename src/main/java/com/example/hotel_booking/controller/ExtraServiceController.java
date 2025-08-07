package com.example.hotel_booking.controller;

import com.example.hotel_booking.entity.ExtraService;
import com.example.hotel_booking.service.IExtraService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/extras")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ExtraServiceController {
    IExtraService extraService;

    @GetMapping
    public ResponseEntity<List<ExtraService>> getAllExtraServices() {
        List<ExtraService> extraServices = extraService.getExtraServices();
        return ResponseEntity.ok(extraServices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtraService> getExtraServiceById(@PathVariable Long id) {
        ExtraService extraServiceById = extraService.getExtraServiceById(id);
        return ResponseEntity.ok(extraServiceById);
    }

    @PostMapping
    public ResponseEntity<ExtraService> createExtraService(@RequestBody ExtraService extraServiceRequest) {
        ExtraService createdExtraService = extraService.createExtraService(extraServiceRequest);
        return ResponseEntity.status(201).body(createdExtraService);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExtraService> updateExtraService(@PathVariable Long id,
                                                           @RequestBody ExtraService extraServiceRequest
    ) {
        ExtraService updatedExtraService = extraService.updateExtraService(id, extraServiceRequest);
        return ResponseEntity.ok(updatedExtraService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExtraService(@PathVariable Long id) {
        extraService.deleteExtraService(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<ExtraService>> getExtraServicesByRoomId(@PathVariable Long roomId) {
        List<ExtraService> extraServices = extraService.getExtraServicesByRoomId(roomId);
        return ResponseEntity.ok(extraServices);
    }
}

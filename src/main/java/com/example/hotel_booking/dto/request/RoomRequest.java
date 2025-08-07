package com.example.hotel_booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequest {
    private String roomType;
    private BigDecimal roomPrice;
    private String roomName;
    private String description;
    private Integer capacity;
    private String roomNumber;
    private Integer quantity;
    private Integer active;
    private List<Long> serviceIds;
}

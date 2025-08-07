package com.example.hotel_booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtraServiceResponse {
    private Long id;
    private String serviceName;
    private String description;
    private BigDecimal price;
}

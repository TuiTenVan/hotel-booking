package com.example.hotel_booking.exception;

import java.io.Serial;

public class RoleException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public RoleException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }
}

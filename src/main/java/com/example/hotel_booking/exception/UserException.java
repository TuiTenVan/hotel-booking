package com.example.hotel_booking.exception;

import java.io.Serial;

public class UserException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UserException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }
}

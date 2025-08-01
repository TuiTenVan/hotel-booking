package com.example.hotel_booking.service;

import com.example.hotel_booking.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IUserService {
    User registerUser(User user);
    List<User> getUsers();
    void deleteUser(String email);
    User getUserByEmail(String email);
}

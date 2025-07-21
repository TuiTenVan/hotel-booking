package com.example.hotel_booking.service;

import com.example.hotel_booking.entity.Role;
import com.example.hotel_booking.entity.User;

import java.util.List;

public interface IRoleService {
    List<Role> getRoles();
    void createRole(Role role);
    void deleteRole(Long id);
    Role findByName(String name);
    User removeUserFromRole(Long userId, Long roleId);
    User assignRoleToUser(Long userId, Long roleId);
    Role removeAllUsersFromRole(Long roleId);
}

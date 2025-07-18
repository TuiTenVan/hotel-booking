package com.example.hotel_booking.repository;

import java.util.Optional;

import com.example.hotel_booking.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String roleUser);
    boolean existsByName(String theRole);
}

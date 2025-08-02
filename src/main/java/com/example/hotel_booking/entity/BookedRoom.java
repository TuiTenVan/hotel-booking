package com.example.hotel_booking.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.hotel_booking.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookedRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;
    private LocalDate checkIn;
    private LocalDate checkOut;

    @Column(name = "guest_name")
    private String guestFullName;

    @Column(name = "guest_email")
    private String guestEmail;

    @Column(name = "children")
    private int numOfChildren;

    @Column(name = "adults")
    private int numOfAdults;

    @Column(name = "total_guest")
    private int totalNumOfGuest;

    @Column(name = "code")
    private String confirmCode;

    @Column(name = "active")
    private Integer active = 1; // 1 - active, 0 - inactive

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public void setNumOfChildren(int numOfChildren) {
        this.numOfChildren = numOfChildren;
        calTotalNumberOfGuest();
    }

    public void setNumOfAdults(int numOfAdults) {
        this.numOfAdults = numOfAdults;
        calTotalNumberOfGuest();
    }

    public void calTotalNumberOfGuest() {
        this.totalNumOfGuest = this.numOfAdults + numOfChildren;
    }
}

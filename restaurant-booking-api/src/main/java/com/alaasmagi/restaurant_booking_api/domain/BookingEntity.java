package com.alaasmagi.restaurant_booking_api.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
public class BookingEntity extends BaseEntity {
    private UUID tableId;
    private String status;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private int peopleCount;
    @ElementCollection
    private List<String> preferences;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

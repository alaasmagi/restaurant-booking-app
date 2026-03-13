package com.alaasmagi.restaurant_booking_api.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import com.alaasmagi.restaurant_booking_api.domain.enums.EBookingStatus;
import com.alaasmagi.restaurant_booking_api.domain.enums.ESeatFeature;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
public class BookingEntity extends BaseEntity {
    private UUID tableId;
    @Enumerated(EnumType.STRING)
    private EBookingStatus status;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private int peopleCount;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<ESeatFeature> preferences;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

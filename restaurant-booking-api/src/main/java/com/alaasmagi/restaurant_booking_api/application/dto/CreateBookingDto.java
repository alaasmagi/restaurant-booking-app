package com.alaasmagi.restaurant_booking_api.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CreateBookingDto {
    private UUID tableId;
    private String clientName;
    private String clientPhone;
    private String clientEmail;
    private int peopleCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

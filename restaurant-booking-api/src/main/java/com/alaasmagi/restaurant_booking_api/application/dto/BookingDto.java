package com.alaasmagi.restaurant_booking_api.application.dto;

import com.alaasmagi.restaurant_booking_api.domain.BookingEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class BookingDto {
    private UUID id;
    private UUID tableId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private int peopleCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public BookingDto(BookingEntity booking) {
        this.id = booking.getId();
        this.tableId = booking.getTableId();
        this.customerName = booking.getCustomerName();
        this.customerPhone = booking.getCustomerPhone();
        this.customerEmail = booking.getCustomerEmail();
        this.peopleCount = booking.getPeopleCount();
        this.startTime = booking.getStartTime();
        this.endTime = booking.getEndTime();
    }
}
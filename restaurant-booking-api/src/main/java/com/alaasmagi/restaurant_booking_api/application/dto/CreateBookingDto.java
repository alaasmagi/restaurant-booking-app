package com.alaasmagi.restaurant_booking_api.application.dto;

import com.alaasmagi.restaurant_booking_api.domain.BookingEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CreateBookingDto {
    private UUID tableId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private int peopleCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public BookingEntity createEntity() {
        BookingEntity booking = new BookingEntity();
        booking.setTableId(this.tableId);
        booking.setCustomerName(this.customerName);
        booking.setCustomerPhone(this.customerPhone);
        booking.setCustomerEmail(this.customerEmail);
        booking.setPeopleCount(this.peopleCount);
        booking.setStartTime(this.startTime);
        booking.setEndTime(this.endTime);
        return booking;
    }
}

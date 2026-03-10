package com.alaasmagi.restaurant_booking_api.application.dtos;

import com.alaasmagi.restaurant_booking_api.domain.BookingEntity;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CreateBookingDto {
    @NotNull(message = "Table ID is required")
    private UUID tableId;

    @NotNull(message = "Customer name is required")
    @Size(min = 1, max = 100, message = "Customer name must be between 1 and 100 characters")
    private String customerName;

    @NotNull(message = "Customer phone is required")
    @Size(min = 5, max = 20, message = "Customer phone must be between 5 and 20 characters")
    private String customerPhone;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Customer email must be at most 100 characters")
    private String customerEmail;

    @NotNull(message = "People count is required")
    @Min(value = 1, message = "At least one person is required for a booking")
    private Integer peopleCount;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
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


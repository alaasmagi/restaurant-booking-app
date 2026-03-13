package com.alaasmagi.restaurant_booking_api.application.dtos;

import com.alaasmagi.restaurant_booking_api.domain.enums.EBookingStatus;
import com.alaasmagi.restaurant_booking_api.domain.enums.ESeatFeature;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class BookingDto {
    @NotNull(message = "Booking ID is required")
    private UUID id;

    @NotNull(message = "Table ID is required")
    private UUID tableId;

    @NotNull(message = "Status is required")
    private EBookingStatus status;

    @NotBlank(message = "Customer name is required")
    @Size(min = 1, max = 100, message = "Customer name must be between 1 and 100 characters")
    private String customerName;

    @NotBlank(message = "Customer phone is required")
    @Size(min = 5, max = 20, message = "Customer phone must be between 5 and 20 characters")
    private String customerPhone;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Customer email must be at most 100 characters")
    private String customerEmail;

    @Min(value = 1, message = "People count must be at least 1")
    private int peopleCount;

    private List<ESeatFeature> preferences;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    public BookingDto() {}
}

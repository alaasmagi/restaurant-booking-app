package com.alaasmagi.restaurant_booking_api.application.dtos;
import com.alaasmagi.restaurant_booking_api.domain.TableEntity;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TableDto {
    @NotNull(message = "Table ID is required")
    private UUID id;

    @Min(value = 1, message = "Seats must be at least 1")
    @Max(value = 100, message = "Seats must be at most 100")
    private int seats;

    @NotBlank(message = "Zone is required")
    @Size(max = 50, message = "Zone must be at most 50 characters")
    private String zone;

    private List<String> features;

    @Min(value = 0, message = "X coordinate must be 0 or greater")
    @Max(value = 10000, message = "X coordinate must be less than or equal to 10000")
    private int x;

    @Min(value = 0, message = "Y coordinate must be 0 or greater")
    @Max(value = 10000, message = "Y coordinate must be less than or equal to 10000")
    private int y;

    private boolean isAvailable;

    public TableDto() {}
}


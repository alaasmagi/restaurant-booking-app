package com.alaasmagi.restaurant_booking_api.application.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionDto {
    @Min(value = 0, message = "X coordinate must be 0 or greater")
    @Max(value = 10000, message = "X coordinate must be less than or equal to 10000")
    private int x;

    @Min(value = 0, message = "Y coordinate must be 0 or greater")
    @Max(value = 10000, message = "Y coordinate must be less than or equal to 10000")
    private int y;
}

package com.alaasmagi.restaurant_booking_api.application.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyPasswordDto {
    private String userName;
    private String password;
}

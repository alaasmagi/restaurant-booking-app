package com.alaasmagi.restaurant_booking_api.application.exceptions;

public class NotFoundException extends ApiException {
    public NotFoundException(String message) {
        super(message);
    }
}

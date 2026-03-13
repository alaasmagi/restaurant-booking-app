package com.alaasmagi.restaurant_booking_api.application.exceptions;

public class ConflictException extends ApiException {
    public ConflictException(String message) {
        super(message);
    }
}

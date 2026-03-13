package com.alaasmagi.restaurant_booking_api.application.exceptions;

public class ValidationException extends ApiException {
    public ValidationException(String message) {
        super(message);
    }
}

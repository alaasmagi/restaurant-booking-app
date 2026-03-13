package com.alaasmagi.restaurant_booking_api.application.exceptions;

public abstract class ApiException extends RuntimeException {
    protected ApiException(String message) {
        super(message);
    }
}

package com.alaasmagi.restaurant_booking_api.infrastructure.web_api.controllers;

import com.alaasmagi.restaurant_booking_api.application.dtos.BookingDto;
import com.alaasmagi.restaurant_booking_api.application.dtos.CreateBookingDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

import com.alaasmagi.restaurant_booking_api.application.BookingService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable UUID id) {
        return bookingService.getBookingById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody CreateBookingDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(request));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable UUID id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok().build();
    }
}

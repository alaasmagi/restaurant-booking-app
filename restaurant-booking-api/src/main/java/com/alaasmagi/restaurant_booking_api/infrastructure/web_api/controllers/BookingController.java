package com.alaasmagi.restaurant_booking_api.infrastructure.web_api.controllers;

import com.alaasmagi.restaurant_booking_api.application.dtos.BookingDto;
import com.alaasmagi.restaurant_booking_api.application.dtos.CreateBookingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.alaasmagi.restaurant_booking_api.application.BookingService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<BookingDto>>> getAllBookings() {
        return bookingService.getAllBookings()
                .thenApply(bookings -> bookings.isEmpty()
                        ? ResponseEntity.noContent().build()
                        : ResponseEntity.ok(bookings));
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<BookingDto>> getBookingById(@PathVariable UUID id) {
        return bookingService.getBookingById(id)
                .thenApply(opt -> opt
                        .map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<BookingDto>> createBooking(@RequestBody CreateBookingDto request) {
        return bookingService.createBooking(request)
                .thenApply(saved -> ResponseEntity.status(HttpStatus.CREATED).body(saved));
    }

    @PatchMapping("/{id}/cancel")
    public CompletableFuture<ResponseEntity<Void>> cancelBooking(@PathVariable UUID id) {
        return bookingService.cancelBooking(id)
                .thenApply(status -> status
                        ? ResponseEntity.ok().build()
                        : ResponseEntity.notFound().build());
    }
}

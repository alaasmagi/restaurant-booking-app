package com.alaasmagi.restaurant_booking_api.infrastructure.web_api;

import com.alaasmagi.restaurant_booking_api.application.dto.BookingDto;
import com.alaasmagi.restaurant_booking_api.application.dto.CreateBookingDto;
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

    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        List<BookingDto> bookings = bookingService.getAllBookings();

        if (bookings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable UUID id) {
        return bookingService.getBookingById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody CreateBookingDto request) {
        BookingDto saved = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PatchMapping("/{id/cancel}")
    public ResponseEntity<Void> cancelBooking(@PathVariable UUID id) {
        boolean status = bookingService.cancelBooking(id);
        if (!status) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }
}

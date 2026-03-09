package com.alaasmagi.restaurant_booking_api.application;

import com.alaasmagi.restaurant_booking_api.application.contracts.IBookingRepository;
import com.alaasmagi.restaurant_booking_api.domain.BookingEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingService {
    private final IBookingRepository bookingRepository;

    public BookingService(IBookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<BookingEntity> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<BookingEntity> getBookingById(UUID id) {
        return bookingRepository.findById(id);
    }

    public List<BookingEntity> getBookingsByTableId(UUID tableId) {
        return bookingRepository.findByTableId(tableId);
    }

    public BookingEntity createBooking(BookingEntity booking) {
        return bookingRepository.save(booking);
    }

    public BookingEntity updateBooking(UUID id, BookingEntity booking) {
        booking.setId(id);
        return bookingRepository.save(booking);
    }

    public void deleteBooking(UUID id) {
        bookingRepository.delete(id);
    }
}

package com.alaasmagi.restaurant_booking_api.application;

import com.alaasmagi.restaurant_booking_api.application.contracts.IBookingRepository;
import com.alaasmagi.restaurant_booking_api.application.dto.BookingDto;
import com.alaasmagi.restaurant_booking_api.application.dto.CreateBookingDto;
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

    public Optional<BookingDto> getBookingById(UUID id) {
        return bookingRepository
                .findById(id)
                .map(BookingDto::new);
    }

    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(BookingDto::new)
                .toList();
    }

    public BookingDto createBooking(CreateBookingDto request) {
        return new BookingDto(bookingRepository.save(request.createEntity()));
    }

    public boolean cancelBooking(UUID id) {
        Optional<BookingEntity> booking = bookingRepository.findById(id);

        if (booking.isEmpty()) {
            return false;
        }

        BookingEntity entity = booking.get();
        entity.setStatus("cancelled");
        bookingRepository.save(entity);
        return true;
    }
}

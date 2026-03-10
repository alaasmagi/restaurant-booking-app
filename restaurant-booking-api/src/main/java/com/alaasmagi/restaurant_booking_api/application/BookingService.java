package com.alaasmagi.restaurant_booking_api.application;

import com.alaasmagi.restaurant_booking_api.application.contracts.IBookingRepository;
import com.alaasmagi.restaurant_booking_api.application.dtos.BookingDto;
import com.alaasmagi.restaurant_booking_api.application.dtos.CreateBookingDto;
import com.alaasmagi.restaurant_booking_api.application.mappers.BookingMapper;
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
                .map(BookingMapper::toDto);
    }

    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(BookingMapper::toDto)
                .toList();
    }

    public BookingDto createBooking(CreateBookingDto request) {
        BookingEntity entity = BookingMapper.fromCreateDto(request);
        entity.setStatus("active");
        return BookingMapper.toDto(bookingRepository.save(entity));
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

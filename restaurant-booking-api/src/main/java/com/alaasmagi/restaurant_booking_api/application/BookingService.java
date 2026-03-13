package com.alaasmagi.restaurant_booking_api.application;

import com.alaasmagi.restaurant_booking_api.application.contracts.IBookingRepository;
import com.alaasmagi.restaurant_booking_api.application.contracts.ITableRepository;
import com.alaasmagi.restaurant_booking_api.application.dtos.BookingDto;
import com.alaasmagi.restaurant_booking_api.application.dtos.CreateBookingDto;
import com.alaasmagi.restaurant_booking_api.application.exceptions.ConflictException;
import com.alaasmagi.restaurant_booking_api.application.exceptions.NotFoundException;
import com.alaasmagi.restaurant_booking_api.application.exceptions.ValidationException;
import com.alaasmagi.restaurant_booking_api.application.mappers.BookingMapper;
import com.alaasmagi.restaurant_booking_api.domain.BookingEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingService {
    private final IBookingRepository bookingRepository;
    private final ITableRepository tableRepository;

    public BookingService(IBookingRepository bookingRepository, ITableRepository tableRepository) {
        this.bookingRepository = bookingRepository;
        this.tableRepository = tableRepository;
    }

    public Optional<BookingDto> getBookingById(UUID id) {
        return bookingRepository.findById(id).map(BookingMapper::toDto);
    }

    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(BookingMapper::toDto)
                .toList();
    }

    @Transactional
    public BookingDto createBooking(CreateBookingDto request) {
        validateBookingRequest(request);
        BookingEntity entity = BookingMapper.fromCreateDto(request);
        entity.activate();
        return BookingMapper.toDto(bookingRepository.save(entity));
    }

    @Transactional
    public void cancelBooking(UUID id) {
        BookingEntity entity = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found for id: " + id));

        if (!entity.canBeCancelled()) {
            throw new ConflictException("Booking is already cancelled");
        }

        entity.cancel();
        bookingRepository.save(entity);
    }

    private void validateBookingRequest(CreateBookingDto request) {
        if (!isValidTimeRange(request.getStartTime(), request.getEndTime())) {
            throw new ValidationException("Booking start time must be before end time");
        }

        var table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new NotFoundException("Table not found for id: " + request.getTableId()));

        if (!table.canAccommodate(request.getPeopleCount())) {
            throw new ValidationException("Booking exceeds the table seating capacity");
        }

        boolean hasOverlap = !bookingRepository.findByTimestamps(request.getStartTime(), request.getEndTime())
                .stream()
                .filter(existing -> request.getTableId().equals(existing.getTableId()))
                .filter(existing -> existing.overlaps(request.getStartTime(), request.getEndTime()))
                .toList()
                .isEmpty();

        if (hasOverlap) {
            throw new ConflictException("Table already has an active booking in the requested time range");
        }
    }

    private boolean isValidTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return startTime != null && endTime != null && startTime.isBefore(endTime);
    }
}

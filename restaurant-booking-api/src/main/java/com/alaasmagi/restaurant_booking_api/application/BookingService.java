package com.alaasmagi.restaurant_booking_api.application;

import com.alaasmagi.restaurant_booking_api.application.contracts.IBookingRepository;
import com.alaasmagi.restaurant_booking_api.application.dtos.BookingDto;
import com.alaasmagi.restaurant_booking_api.application.dtos.CreateBookingDto;
import com.alaasmagi.restaurant_booking_api.application.mappers.BookingMapper;
import com.alaasmagi.restaurant_booking_api.domain.BookingEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class BookingService {
    private final IBookingRepository bookingRepository;

    public BookingService(IBookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Async("taskExecutor")
    public CompletableFuture<Optional<BookingDto>> getBookingById(UUID id) {
        return CompletableFuture.completedFuture(
                bookingRepository.findById(id).map(BookingMapper::toDto)
        );
    }

    @Async("taskExecutor")
    public CompletableFuture<List<BookingDto>> getAllBookings() {
        return CompletableFuture.completedFuture(
                bookingRepository.findAll()
                        .stream()
                        .map(BookingMapper::toDto)
                        .toList()
        );
    }

    @Async("taskExecutor")
    public CompletableFuture<BookingDto> createBooking(CreateBookingDto request) {
        BookingEntity entity = BookingMapper.fromCreateDto(request);
        entity.setStatus("active");
        return CompletableFuture.completedFuture(BookingMapper.toDto(bookingRepository.save(entity)));
    }

    @Async("taskExecutor")
    public CompletableFuture<Boolean> cancelBooking(UUID id) {
        Optional<BookingEntity> booking = bookingRepository.findById(id);

        if (booking.isEmpty()) {
            return CompletableFuture.completedFuture(false);
        }

        BookingEntity entity = booking.get();
        entity.setStatus("cancelled");
        bookingRepository.save(entity);
        return CompletableFuture.completedFuture(true);
    }
}

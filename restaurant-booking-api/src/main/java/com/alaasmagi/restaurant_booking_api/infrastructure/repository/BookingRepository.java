package com.alaasmagi.restaurant_booking_api.infrastructure.repository;

import com.alaasmagi.restaurant_booking_api.application.contracts.IBookingRepository;
import com.alaasmagi.restaurant_booking_api.domain.BookingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BookingRepository implements IBookingRepository {
    private final BookingJpaRepository bookingJpaRepository;

    @Autowired
    public BookingRepository(BookingJpaRepository bookingJpaRepository) {
        this.bookingJpaRepository = bookingJpaRepository;
    }

    @Override
    public List<BookingEntity> findByTableId(UUID tableId) {
        return bookingJpaRepository.findByTableId(tableId);
    }

    @Override
    public List<BookingEntity> findByTimestamps(LocalDateTime startTime, LocalDateTime endTime) {
        return bookingJpaRepository.findByStatusAndStartTimeBeforeAndEndTimeAfter("active", endTime, startTime);
    }

    @Override
    public List<BookingEntity> findAll() {
        return bookingJpaRepository.findAll();
    }

    @Override
    public Optional<BookingEntity> findById(UUID uuid) {
        return bookingJpaRepository.findById(uuid);
    }

    @Override
    public BookingEntity save(BookingEntity entity) {
        return bookingJpaRepository.save(entity);
    }

    @Override
    public void delete(UUID uuid) {
        bookingJpaRepository.deleteById(uuid);
    }
}

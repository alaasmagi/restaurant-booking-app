package com.alaasmagi.restaurant_booking_api.infrastructure.repository;

import com.alaasmagi.restaurant_booking_api.application.contracts.IBookingRepository;
import com.alaasmagi.restaurant_booking_api.domain.BookingEntity;
import com.alaasmagi.restaurant_booking_api.domain.enums.EBookingStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BookingRepository implements IBookingRepository {
    private final IBookingJpaRepository IBookingJpaRepository;

    public BookingRepository(IBookingJpaRepository IBookingJpaRepository) {
        this.IBookingJpaRepository = IBookingJpaRepository;
    }

    @Override
    public List<BookingEntity> findByTableId(UUID tableId) {
        return IBookingJpaRepository.findByTableId(tableId);
    }

    @Override
    public List<BookingEntity> findByTimestamps(LocalDateTime startTime, LocalDateTime endTime) {
        return IBookingJpaRepository.findByStatusAndStartTimeBeforeAndEndTimeAfter(EBookingStatus.ACTIVE, endTime, startTime);
    }

    @Override
    public List<BookingEntity> findAll() {
        return IBookingJpaRepository.findAll();
    }

    @Override
    public Optional<BookingEntity> findById(UUID uuid) {
        return IBookingJpaRepository.findById(uuid);
    }

    @Override
    public BookingEntity save(BookingEntity entity) {
        return IBookingJpaRepository.save(entity);
    }

    @Override
    public void delete(UUID uuid) {
        IBookingJpaRepository.deleteById(uuid);
    }
}

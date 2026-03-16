package com.alaasmagi.restaurant_booking_api.infrastructure.repository;

import com.alaasmagi.restaurant_booking_api.domain.BookingEntity;
import com.alaasmagi.restaurant_booking_api.domain.enums.EBookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface IBookingJpaRepository extends JpaRepository<BookingEntity, UUID> {
    List<BookingEntity> findByTableId(UUID tableId);
    List<BookingEntity> findByStatusAndStartTimeBeforeAndEndTimeAfter(EBookingStatus status, LocalDateTime endTime, LocalDateTime startTime);
}

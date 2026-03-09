package com.alaasmagi.restaurant_booking_api.application.contracts;

import com.alaasmagi.restaurant_booking_api.domain.BookingEntity;

import java.util.List;
import java.util.UUID;

public interface IBookingRepository extends IRepository<BookingEntity, UUID> {
    List<BookingEntity> findByTableId(UUID tableId);
}

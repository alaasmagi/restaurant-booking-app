package com.alaasmagi.restaurant_booking_api.infrastructure.repository;

import com.alaasmagi.restaurant_booking_api.application.contracts.IBookingRepository;
import com.alaasmagi.restaurant_booking_api.domain.BookingEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Repository
public class BookingRepository implements IBookingRepository {
    private final ConcurrentMap<UUID, BookingEntity> bookingStore = new ConcurrentHashMap<>();

    @Override
    public List<BookingEntity> findByTableId(UUID tableId) {
        return bookingStore.values().stream()
                .filter(booking -> tableId != null && tableId.equals(booking.getTableId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingEntity> findByTimestamps(LocalDateTime endTime, LocalDateTime startTime) {
        return bookingStore.values().stream()
                .filter(booking ->
                        Objects.equals(booking.getStatus(), "active") &&
                        booking.getStartTime().isBefore(endTime) &&
                        booking.getEndTime().isAfter(startTime))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingEntity> findAll() {
        return List.copyOf(bookingStore.values());
    }

    @Override
    public Optional<BookingEntity> findById(UUID uuid) {
        return Optional.ofNullable(bookingStore.get(uuid));
    }

    @Override
    public BookingEntity save(BookingEntity entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        bookingStore.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void delete(UUID uuid) {
        bookingStore.remove(uuid);
    }
}

package com.alaasmagi.restaurant_booking_api.application;

import com.alaasmagi.restaurant_booking_api.application.contracts.IBookingRepository;
import com.alaasmagi.restaurant_booking_api.application.contracts.ITableRepository;
import com.alaasmagi.restaurant_booking_api.application.dtos.PositionDto;
import com.alaasmagi.restaurant_booking_api.application.dtos.TableDto;
import com.alaasmagi.restaurant_booking_api.application.exceptions.NotFoundException;
import com.alaasmagi.restaurant_booking_api.application.exceptions.ValidationException;
import com.alaasmagi.restaurant_booking_api.application.mappers.TableMapper;
import com.alaasmagi.restaurant_booking_api.domain.BookingEntity;
import com.alaasmagi.restaurant_booking_api.domain.TableEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Service
public class TableService {
    private final ITableRepository tableRepository;
    private final IBookingRepository bookingRepository;

    public TableService(ITableRepository tableRepository, IBookingRepository bookingRepository) {
        this.tableRepository = tableRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<TableDto> getAllTables(LocalDateTime start, LocalDateTime end) {
        LocalDateTime effectiveStart = start != null ? start : LocalDateTime.now();
        LocalDateTime effectiveEnd = end != null ? end : LocalDateTime.now();

        if (effectiveStart.isAfter(effectiveEnd)) {
            throw new ValidationException("Start time must be before end time");
        }

        Set<UUID> occupiedTableIds = new HashSet<>(bookingRepository
                .findByTimestamps(effectiveStart, effectiveEnd)
                .stream()
                .map(BookingEntity::getTableId)
                .toList());

        return tableRepository.findAll()
                .stream()
                .map(table -> TableMapper.toDto(table, !occupiedTableIds.contains(table.getId())))
                .toList();
    }

    @Transactional
    public void setTablePosition(UUID id, PositionDto newPosition) {
        TableEntity updatedTable = tableRepository.changePosition(id, newPosition.getX(), newPosition.getY());

        if (updatedTable == null) {
            throw new NotFoundException("Table not found for id: " + id);
        }
    }
}

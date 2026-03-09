package com.alaasmagi.restaurant_booking_api.application;

import com.alaasmagi.restaurant_booking_api.application.contracts.IBookingRepository;
import com.alaasmagi.restaurant_booking_api.application.contracts.ITableRepository;
import com.alaasmagi.restaurant_booking_api.application.dto.TableDto;
import com.alaasmagi.restaurant_booking_api.domain.BookingEntity;
import com.alaasmagi.restaurant_booking_api.domain.TableEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TableService {
    private final ITableRepository tableRepository;
    private final IBookingRepository bookingRepository;

    public TableService(ITableRepository tableRepository, IBookingRepository bookingRepository) {
        this.tableRepository = tableRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<TableDto> getAllTablesWithAvailability(LocalDateTime start, LocalDateTime end) {
        List<UUID> occupiedTableIds = bookingRepository
                .findByTimestamps(end, start)
                .stream()
                .map(BookingEntity::getTableId)
                .toList();

        return tableRepository.findAll()
                .stream()
                .map(table -> new TableDto(table, !occupiedTableIds.contains(table.getId())))
                .toList();
    }
    public List<TableEntity> getAllTables() {
        return tableRepository.findAll();
    }

    public Optional<TableEntity> getTableById(UUID id) {
        return tableRepository.findById(id);
    }

    public List<TableEntity> getTablesByZone(String zone) {
        return tableRepository.findByZone(zone);
    }

    public List<TableEntity> getTablesBySeatsGreaterThanEqual(int seats) {
        return tableRepository.findBySeatsGreaterThanEqual(seats);
    }

    public TableEntity createTable(TableEntity table) {
        return tableRepository.save(table);
    }

    public TableEntity updateTable(UUID id, TableEntity table) {
        table.setId(id);
        return tableRepository.save(table);
    }

    public void deleteTable(UUID id) {
        tableRepository.delete(id);
    }

    public TableEntity saveTable(TableEntity table) {
        return tableRepository.save(table);
    }
}

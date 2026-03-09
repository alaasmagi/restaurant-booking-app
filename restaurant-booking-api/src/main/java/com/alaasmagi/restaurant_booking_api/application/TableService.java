package com.alaasmagi.restaurant_booking_api.application;

import com.alaasmagi.restaurant_booking_api.application.contracts.ITableRepository;
import com.alaasmagi.restaurant_booking_api.domain.TableEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TableService {
    private final ITableRepository tableRepository;

    public TableService(ITableRepository tableRepository) {
        this.tableRepository = tableRepository;
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
}

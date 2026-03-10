package com.alaasmagi.restaurant_booking_api.infrastructure.repository;

import com.alaasmagi.restaurant_booking_api.application.contracts.ITableRepository;
import com.alaasmagi.restaurant_booking_api.domain.TableEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.awt.Point;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TableRepository implements ITableRepository {
    private final TableJpaRepository tableJpaRepository;

    @Autowired
    public TableRepository(TableJpaRepository tableJpaRepository) {
        this.tableJpaRepository = tableJpaRepository;
    }

    @Override
    public TableEntity changePosition(UUID id, double x, double y) {
        Optional<TableEntity> optionalTable = tableJpaRepository.findById(id);
        if (optionalTable.isEmpty()) {
            return null;
        }
        TableEntity table = optionalTable.get();
        table.setPosition(new Point((int)x, (int)y));
        return tableJpaRepository.save(table);
    }

    @Override
    public List<TableEntity> findAll() {
        return tableJpaRepository.findAll();
    }

    @Override
    public Optional<TableEntity> findById(UUID uuid) {
        return tableJpaRepository.findById(uuid);
    }

    @Override
    public TableEntity save(TableEntity entity) {
        return tableJpaRepository.save(entity);
    }

    @Override
    public void delete(UUID uuid) {
        tableJpaRepository.deleteById(uuid);
    }
}

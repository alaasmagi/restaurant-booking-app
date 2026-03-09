package com.alaasmagi.restaurant_booking_api.infrastructure.repository;

import com.alaasmagi.restaurant_booking_api.application.contracts.ITableRepository;
import com.alaasmagi.restaurant_booking_api.domain.TableEntity;
import org.springframework.stereotype.Repository;

import java.awt.Point;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Repository
public class TableRepository implements ITableRepository {
    private final ConcurrentMap<UUID, TableEntity> tableStore = new ConcurrentHashMap<>();

    @Override
    public List<TableEntity> findByZone(String zone) {
        return tableStore.values().stream()
                .filter(table -> zone != null && zone.equals(table.getZone()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TableEntity> findBySeatsGreaterThanEqual(int seats) {
        return tableStore.values().stream()
                .filter(table -> table.getSeats() >= seats)
                .collect(Collectors.toList());
    }

    @Override
    public TableEntity changePosition(UUID id, double x, double y) {
        TableEntity table = tableStore.get(id);
        if (table == null) {
            return null;
        }
        table.setPosition(new Point((int)x, (int)y));
        tableStore.put(id, table);
        return table;
    }

    @Override
    public List<TableEntity> findAll() {
        return List.copyOf(tableStore.values());
    }

    @Override
    public Optional<TableEntity> findById(UUID uuid) {
        return Optional.ofNullable(tableStore.get(uuid));
    }

    @Override
    public TableEntity save(TableEntity entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        tableStore.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void delete(UUID uuid) {
        tableStore.remove(uuid);
    }
}

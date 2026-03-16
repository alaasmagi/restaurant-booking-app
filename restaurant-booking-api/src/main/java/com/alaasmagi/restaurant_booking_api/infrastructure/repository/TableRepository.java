package com.alaasmagi.restaurant_booking_api.infrastructure.repository;

import com.alaasmagi.restaurant_booking_api.application.contracts.ITableRepository;
import com.alaasmagi.restaurant_booking_api.domain.TableEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TableRepository implements ITableRepository {
    private final ITableJpaRepository ITableJpaRepository;

    public TableRepository(ITableJpaRepository ITableJpaRepository) {
        this.ITableJpaRepository = ITableJpaRepository;
    }

    @Override
    public TableEntity changePosition(UUID id, int x, int y) {
        Optional<TableEntity> optionalTable = ITableJpaRepository.findById(id);
        if (optionalTable.isEmpty()) {
            return null;
        }
        TableEntity table = optionalTable.get();
        table.setX(x);
        table.setY(y);
        return ITableJpaRepository.save(table);
    }

    @Override
    public List<TableEntity> findAll() {
        return ITableJpaRepository.findAll();
    }

    @Override
    public Optional<TableEntity> findById(UUID uuid) {
        return ITableJpaRepository.findById(uuid);
    }

    @Override
    public TableEntity save(TableEntity entity) {
        return ITableJpaRepository.save(entity);
    }

    @Override
    public void delete(UUID uuid) {
        ITableJpaRepository.deleteById(uuid);
    }
}

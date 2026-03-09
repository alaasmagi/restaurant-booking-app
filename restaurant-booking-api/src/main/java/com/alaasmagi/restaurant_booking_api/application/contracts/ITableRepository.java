package com.alaasmagi.restaurant_booking_api.application.contracts;

import com.alaasmagi.restaurant_booking_api.domain.TableEntity;

import java.util.List;
import java.util.UUID;

public interface ITableRepository extends IRepository<TableEntity, UUID> {
    List<TableEntity> findByZone(String zone);
    List<TableEntity> findBySeatsGreaterThanEqual(int seats);
}

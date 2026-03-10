package com.alaasmagi.restaurant_booking_api.application.contracts;

import com.alaasmagi.restaurant_booking_api.domain.TableEntity;

import java.util.UUID;

public interface ITableRepository extends IRepository<TableEntity, UUID> {
    TableEntity changePosition(UUID id, double x, double y);
}

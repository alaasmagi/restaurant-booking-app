package com.alaasmagi.restaurant_booking_api.application.mappers;

import com.alaasmagi.restaurant_booking_api.application.dtos.TableDto;
import com.alaasmagi.restaurant_booking_api.domain.TableEntity;

public class TableMapper {
    public static TableDto toDto(TableEntity entity, boolean isAvailable) {
        if (entity == null) return null;
        TableDto dto = new TableDto();
        dto.setId(entity.getId());
        dto.setSeats(entity.getSeats());
        dto.setZone(entity.getZone());
        dto.setFeatures(entity.getFeatures());
        if (entity.getPosition() != null) {
            dto.setX(entity.getPosition().x);
            dto.setY(entity.getPosition().y);
        }
        dto.setAvailable(isAvailable);
        return dto;
    }
}

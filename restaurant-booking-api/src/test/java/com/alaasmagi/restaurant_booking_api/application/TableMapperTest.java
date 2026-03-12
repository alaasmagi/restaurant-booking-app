package com.alaasmagi.restaurant_booking_api.application;

import com.alaasmagi.restaurant_booking_api.application.dtos.TableDto;
import com.alaasmagi.restaurant_booking_api.application.mappers.TableMapper;
import com.alaasmagi.restaurant_booking_api.domain.TableEntity;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TableMapperTest {

    @Test
    void toDto_mapsAllFieldsCorrectly_andAvailableTrue() {
        UUID id = UUID.randomUUID();
        TableEntity entity = new TableEntity();
        entity.setId(id);
        entity.setSeats(6);
        entity.setZone("terrace");
        entity.setFeatures(List.of("outdoor", "heater"));
        entity.setPosition(new Point(50, 75));

        TableDto dto = TableMapper.toDto(entity, true);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getSeats()).isEqualTo(6);
        assertThat(dto.getZone()).isEqualTo("terrace");
        assertThat(dto.getFeatures()).containsExactly("outdoor", "heater");
        assertThat(dto.getX()).isEqualTo(50);
        assertThat(dto.getY()).isEqualTo(75);
        assertThat(dto.isAvailable()).isTrue();
    }

    @Test
    void toDto_setsAvailableFalse_whenOccupied() {
        TableEntity entity = new TableEntity();
        entity.setId(UUID.randomUUID());
        entity.setSeats(2);
        entity.setZone("main");
        entity.setPosition(new Point(0, 0));

        TableDto dto = TableMapper.toDto(entity, false);

        assertThat(dto.isAvailable()).isFalse();
    }

    @Test
    void toDto_whenPositionIsNull_setsCoordinatesToZero() {
        TableEntity entity = new TableEntity();
        entity.setId(UUID.randomUUID());
        entity.setSeats(4);
        entity.setZone("bar");
        entity.setPosition(null);

        TableDto dto = TableMapper.toDto(entity, true);

        assertThat(dto.getX()).isEqualTo(0);
        assertThat(dto.getY()).isEqualTo(0);
    }

    @Test
    void toDto_whenNullEntity_returnsNull() {
        assertThat(TableMapper.toDto(null, true)).isNull();
    }
}


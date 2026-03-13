package com.alaasmagi.restaurant_booking_api.application;

import com.alaasmagi.restaurant_booking_api.application.dtos.BookingDto;
import com.alaasmagi.restaurant_booking_api.application.dtos.CreateBookingDto;
import com.alaasmagi.restaurant_booking_api.application.mappers.BookingMapper;
import com.alaasmagi.restaurant_booking_api.domain.BookingEntity;
import com.alaasmagi.restaurant_booking_api.domain.enums.EBookingStatus;
import com.alaasmagi.restaurant_booking_api.domain.enums.ESeatFeature;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class BookingMapperTest {

    @Test
    void toDto_mapsAllFieldsCorrectly() {
        UUID id = UUID.randomUUID();
        UUID tableId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2026, 5, 1, 18, 0);
        LocalDateTime end = LocalDateTime.of(2026, 5, 1, 20, 0);

        BookingEntity entity = new BookingEntity();
        entity.setId(id);
        entity.setTableId(tableId);
        entity.setStatus(EBookingStatus.ACTIVE);
        entity.setCustomerName("Alice Smith");
        entity.setCustomerPhone("555-1234");
        entity.setCustomerEmail("alice@example.com");
        entity.setPeopleCount(3);
        entity.setPreferences(List.of(ESeatFeature.WINDOW, ESeatFeature.ACCESSIBLE));
        entity.setStartTime(start);
        entity.setEndTime(end);

        BookingDto dto = BookingMapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getTableId()).isEqualTo(tableId);
        assertThat(dto.getStatus()).isEqualTo(EBookingStatus.ACTIVE);
        assertThat(dto.getCustomerName()).isEqualTo("Alice Smith");
        assertThat(dto.getCustomerPhone()).isEqualTo("555-1234");
        assertThat(dto.getCustomerEmail()).isEqualTo("alice@example.com");
        assertThat(dto.getPeopleCount()).isEqualTo(3);
        assertThat(dto.getPreferences()).containsExactly(ESeatFeature.WINDOW, ESeatFeature.ACCESSIBLE);
        assertThat(dto.getStartTime()).isEqualTo(start);
        assertThat(dto.getEndTime()).isEqualTo(end);
    }

    @Test
    void toDto_whenNullEntity_returnsNull() {
        assertThat(BookingMapper.toDto(null)).isNull();
    }

    @Test
    void fromCreateDto_mapsAllFieldsCorrectly() {
        UUID tableId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2026, 5, 1, 18, 0);
        LocalDateTime end = LocalDateTime.of(2026, 5, 1, 20, 0);

        CreateBookingDto dto = new CreateBookingDto();
        dto.setTableId(tableId);
        dto.setCustomerName("Bob Jones");
        dto.setCustomerPhone("555-9876");
        dto.setCustomerEmail("bob@example.com");
        dto.setPeopleCount(4);
        dto.setPreferences(List.of(ESeatFeature.PRIVATE, ESeatFeature.OUTDOOR));
        dto.setStartTime(start);
        dto.setEndTime(end);

        BookingEntity entity = BookingMapper.fromCreateDto(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getTableId()).isEqualTo(tableId);
        assertThat(entity.getCustomerName()).isEqualTo("Bob Jones");
        assertThat(entity.getCustomerPhone()).isEqualTo("555-9876");
        assertThat(entity.getCustomerEmail()).isEqualTo("bob@example.com");
        assertThat(entity.getPeopleCount()).isEqualTo(4);
        assertThat(entity.getPreferences()).containsExactly(ESeatFeature.PRIVATE, ESeatFeature.OUTDOOR);
        assertThat(entity.getStartTime()).isEqualTo(start);
        assertThat(entity.getEndTime()).isEqualTo(end);
    }

    @Test
    void fromCreateDto_whenNullDto_returnsNull() {
        assertThat(BookingMapper.fromCreateDto(null)).isNull();
    }
}

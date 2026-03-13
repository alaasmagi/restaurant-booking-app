package com.alaasmagi.restaurant_booking_api.application.mappers;

import com.alaasmagi.restaurant_booking_api.application.dtos.BookingDto;
import com.alaasmagi.restaurant_booking_api.application.dtos.CreateBookingDto;
import com.alaasmagi.restaurant_booking_api.domain.BookingEntity;

public class BookingMapper {
    public static BookingDto toDto(BookingEntity entity) {
        if (entity == null) return null;
        BookingDto dto = new BookingDto();
        dto.setId(entity.getId());
        dto.setTableId(entity.getTableId());
        dto.setStatus(entity.getStatus());
        dto.setCustomerName(entity.getCustomerName());
        dto.setCustomerPhone(entity.getCustomerPhone());
        dto.setCustomerEmail(entity.getCustomerEmail());
        dto.setPeopleCount(entity.getPeopleCount());
        dto.setPreferences(entity.getPreferences());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        return dto;
    }

    public static BookingEntity fromCreateDto(CreateBookingDto dto) {
        if (dto == null) return null;
        BookingEntity entity = new BookingEntity();
        entity.setTableId(dto.getTableId());
        entity.setCustomerName(dto.getCustomerName());
        entity.setCustomerPhone(dto.getCustomerPhone());
        entity.setCustomerEmail(dto.getCustomerEmail());
        entity.setPeopleCount(dto.getPeopleCount());
        entity.setPreferences(dto.getPreferences());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        return entity;
    }
}

package com.alaasmagi.restaurant_booking_api.application.dto;
import com.alaasmagi.restaurant_booking_api.domain.TableEntity;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TableDto {
    private UUID id;
    private int seats;
    private String zone;
    private List<String> features;
    private double x;
    private double y;

    public TableDto(TableEntity table) {
        this.id = table.getId();
        this.seats = table.getSeats();
        this.zone = table.getZone();
        this.features = table.getFeatures();
        this.x = table.getPosition().getX();
        this.y = table.getPosition().getY();
    }
}

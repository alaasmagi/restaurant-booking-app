package com.alaasmagi.restaurant_booking_api.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.List;

@Getter
@Setter
@Entity
public class TableEntity extends BaseEntity {
    private int seats;
    private String zone;
    private List<String> features;
    private Point position;
}

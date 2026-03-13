package com.alaasmagi.restaurant_booking_api.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ElementCollection;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class TableEntity extends BaseEntity {
    private int seats;
    private String zone;
    @ElementCollection
    private List<String> features;
    private int x;
    private int y;
}

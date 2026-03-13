package com.alaasmagi.restaurant_booking_api.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import com.alaasmagi.restaurant_booking_api.domain.enums.ESeatFeature;

import java.util.List;

@Getter
@Setter
@Entity
public class TableEntity extends BaseEntity {
    private int seats;
    private String zone;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<ESeatFeature> features;
    private int x;
    private int y;
}

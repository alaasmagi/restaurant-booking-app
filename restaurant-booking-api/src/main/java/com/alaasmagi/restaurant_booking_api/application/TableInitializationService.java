package com.alaasmagi.restaurant_booking_api.application;

import com.alaasmagi.restaurant_booking_api.domain.TableEntity;
import com.alaasmagi.restaurant_booking_api.infrastructure.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;

@RequiredArgsConstructor
@Component
public class TableInitializationService implements CommandLineRunner {
    private final TableRepository tableRepository;

    @Override
    public void run(String @NonNull ... args) {
        if (tableRepository.findAll().isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                TableEntity table = new TableEntity();
                table.setSeats(4);
                table.setZone("main");
                table.setCreatedBy("initialization");
                table.setUpdatedBy("initialization");
                table.setFeatures(List.of());
                table.setPosition(new Point(i, 0));
                tableRepository.save(table);
            }
        }
    }
}


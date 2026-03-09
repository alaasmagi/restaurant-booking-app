package com.alaasmagi.restaurant_booking_api.application;

import com.alaasmagi.restaurant_booking_api.domain.TableEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;

@Component
public class TableInitializationService implements CommandLineRunner {
    private final TableService tableService;

    public TableInitializationService(TableService tableService) {
        this.tableService = tableService;
    }

    @Override
    public void run(String @NonNull ... args) {
        if (tableService.getAllTables().isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                TableEntity table = new TableEntity();
                table.setSeats(4);
                table.setZone("main");
                table.setCreatedBy("initialization");
                table.setUpdatedBy("initialization");
                table.setFeatures(List.of());
                table.setPosition(new Point(i, 0));
                tableService.saveTable(table);
            }
        }
    }
}


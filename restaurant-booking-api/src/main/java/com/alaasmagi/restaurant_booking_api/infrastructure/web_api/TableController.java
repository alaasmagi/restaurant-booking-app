package com.alaasmagi.restaurant_booking_api.infrastructure.web_api;

import com.alaasmagi.restaurant_booking_api.domain.TableEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.UUID;

import com.alaasmagi.restaurant_booking_api.application.TableService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/tables")
public class TableController {
    private final TableService tableService;

    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping
    public List<TableEntity> getAllTables() {
        return tableService.getAllTables();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableEntity> getTableById(@PathVariable UUID id) {
        return tableService.getTableById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/zone/{zone}")
    public List<TableEntity> getTablesByZone(@PathVariable String zone) {
        return tableService.getTablesByZone(zone);
    }

    @GetMapping("/seats/{seats}")
    public List<TableEntity> getTablesBySeatsGreaterThanEqual(@PathVariable int seats) {
        return tableService.getTablesBySeatsGreaterThanEqual(seats);
    }

    @PostMapping
    public ResponseEntity<TableEntity> createTable(@RequestBody TableEntity table) {
        TableEntity saved = tableService.createTable(table);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TableEntity> updateTable(@PathVariable UUID id, @RequestBody TableEntity table) {
        if (tableService.getTableById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        TableEntity updated = tableService.updateTable(id, table);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable UUID id) {
        if (tableService.getTableById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        tableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }
}

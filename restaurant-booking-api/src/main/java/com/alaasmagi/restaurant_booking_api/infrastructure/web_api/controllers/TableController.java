package com.alaasmagi.restaurant_booking_api.infrastructure.web_api.controllers;

import com.alaasmagi.restaurant_booking_api.application.dtos.PositionDto;
import com.alaasmagi.restaurant_booking_api.application.dtos.TableDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.alaasmagi.restaurant_booking_api.application.TableService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tables")
public class TableController {
    private final TableService tableService;

    @GetMapping
    public List<TableDto> getTables(@RequestParam(required = false) LocalDateTime startTime,
                                    @RequestParam(required = false) LocalDateTime endTime) {
        return tableService.getAllTables(startTime, endTime);
    }

    @PatchMapping("/{id}/position")
    public ResponseEntity<Void> setPosition(@PathVariable UUID id, @RequestBody PositionDto newPosition) {
        boolean status = tableService.setTablePosition(id, newPosition);

        if (!status) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}

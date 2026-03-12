package com.alaasmagi.restaurant_booking_api.application;

import com.alaasmagi.restaurant_booking_api.application.contracts.IBookingRepository;
import com.alaasmagi.restaurant_booking_api.application.contracts.ITableRepository;
import com.alaasmagi.restaurant_booking_api.application.dtos.PositionDto;
import com.alaasmagi.restaurant_booking_api.application.dtos.TableDto;
import com.alaasmagi.restaurant_booking_api.domain.BookingEntity;
import com.alaasmagi.restaurant_booking_api.domain.TableEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private ITableRepository tableRepository;

    @Mock
    private IBookingRepository bookingRepository;

    @InjectMocks
    private TableService tableService;

    private UUID tableId;
    private TableEntity tableEntity;

    @BeforeEach
    void setUp() {
        tableId = UUID.randomUUID();
        tableEntity = new TableEntity();
        tableEntity.setId(tableId);
        tableEntity.setSeats(4);
        tableEntity.setZone("main");
        tableEntity.setFeatures(List.of("window", "quiet"));
        tableEntity.setPosition(new Point(100, 200));
    }

    @Test
    void getAllTables_withNoOccupiedTables_marksAllAvailable() throws ExecutionException, InterruptedException {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(3);

        when(bookingRepository.findByTimestamps(start, end)).thenReturn(List.of());
        when(tableRepository.findAll()).thenReturn(List.of(tableEntity));

        List<TableDto> result = tableService.getAllTables(start, end).get();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(tableId);
        assertThat(result.getFirst().isAvailable()).isTrue();
    }

    @Test
    void getAllTables_withOccupiedTable_marksTableUnavailable() throws ExecutionException, InterruptedException {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(3);

        BookingEntity booking = new BookingEntity();
        booking.setTableId(tableId);
        booking.setStatus("active");

        when(bookingRepository.findByTimestamps(start, end)).thenReturn(List.of(booking));
        when(tableRepository.findAll()).thenReturn(List.of(tableEntity));

        List<TableDto> result = tableService.getAllTables(start, end).get();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().isAvailable()).isFalse();
    }

    @Test
    void getAllTables_withNullTimestamps_usesCurrentTime() throws ExecutionException, InterruptedException {
        when(bookingRepository.findByTimestamps(any(), any())).thenReturn(List.of());
        when(tableRepository.findAll()).thenReturn(List.of(tableEntity));

        List<TableDto> result = tableService.getAllTables(null, null).get();

        assertThat(result).hasSize(1);
        verify(bookingRepository, times(1)).findByTimestamps(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void getAllTables_mapsPositionCorrectly() throws ExecutionException, InterruptedException {
        when(bookingRepository.findByTimestamps(any(), any())).thenReturn(List.of());
        when(tableRepository.findAll()).thenReturn(List.of(tableEntity));

        List<TableDto> result = tableService.getAllTables(null, null).get();

        assertThat(result.getFirst().getX()).isEqualTo(100);
        assertThat(result.getFirst().getY()).isEqualTo(200);
        assertThat(result.getFirst().getZone()).isEqualTo("main");
        assertThat(result.getFirst().getSeats()).isEqualTo(4);
    }

    @Test
    void setTablePosition_whenTableExists_returnsTrue() throws ExecutionException, InterruptedException {
        PositionDto positionDto = new PositionDto();
        positionDto.setX(300);
        positionDto.setY(400);

        when(tableRepository.changePosition(tableId, 300, 400)).thenReturn(tableEntity);

        Boolean result = tableService.setTablePosition(tableId, positionDto).get();

        assertThat(result).isTrue();
        verify(tableRepository, times(1)).changePosition(tableId, 300, 400);
    }

    @Test
    void setTablePosition_whenTableNotFound_returnsFalse() throws ExecutionException, InterruptedException {
        PositionDto positionDto = new PositionDto();
        positionDto.setX(300);
        positionDto.setY(400);

        when(tableRepository.changePosition(tableId, 300, 400)).thenReturn(null);

        Boolean result = tableService.setTablePosition(tableId, positionDto).get();

        assertThat(result).isFalse();
    }
}


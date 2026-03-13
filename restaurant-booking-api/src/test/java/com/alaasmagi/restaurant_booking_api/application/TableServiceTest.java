package com.alaasmagi.restaurant_booking_api.application;

import com.alaasmagi.restaurant_booking_api.application.contracts.IBookingRepository;
import com.alaasmagi.restaurant_booking_api.application.contracts.ITableRepository;
import com.alaasmagi.restaurant_booking_api.application.dtos.PositionDto;
import com.alaasmagi.restaurant_booking_api.application.dtos.TableDto;
import com.alaasmagi.restaurant_booking_api.application.exceptions.NotFoundException;
import com.alaasmagi.restaurant_booking_api.application.exceptions.ValidationException;
import com.alaasmagi.restaurant_booking_api.domain.BookingEntity;
import com.alaasmagi.restaurant_booking_api.domain.TableEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        tableEntity.setX(100);
        tableEntity.setY(200);
    }

    @Test
    void getAllTables_withNoOccupiedTables_marksAllAvailable() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(3);

        when(bookingRepository.findByTimestamps(start, end)).thenReturn(List.of());
        when(tableRepository.findAll()).thenReturn(List.of(tableEntity));

        List<TableDto> result = tableService.getAllTables(start, end);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(tableId);
        assertThat(result.getFirst().isAvailable()).isTrue();
    }

    @Test
    void getAllTables_withOccupiedTable_marksTableUnavailable() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(3);

        BookingEntity booking = new BookingEntity();
        booking.setTableId(tableId);
        booking.setStatus("active");

        when(bookingRepository.findByTimestamps(start, end)).thenReturn(List.of(booking));
        when(tableRepository.findAll()).thenReturn(List.of(tableEntity));

        List<TableDto> result = tableService.getAllTables(start, end);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().isAvailable()).isFalse();
    }

    @Test
    void getAllTables_withNullTimestamps_usesCurrentTime() {
        when(bookingRepository.findByTimestamps(any(), any())).thenReturn(List.of());
        when(tableRepository.findAll()).thenReturn(List.of(tableEntity));

        List<TableDto> result = tableService.getAllTables(null, null);

        assertThat(result).hasSize(1);
        verify(bookingRepository, times(1)).findByTimestamps(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void getAllTables_mapsPositionCorrectly() {
        when(bookingRepository.findByTimestamps(any(), any())).thenReturn(List.of());
        when(tableRepository.findAll()).thenReturn(List.of(tableEntity));

        List<TableDto> result = tableService.getAllTables(null, null);

        assertThat(result.getFirst().getX()).isEqualTo(100);
        assertThat(result.getFirst().getY()).isEqualTo(200);
        assertThat(result.getFirst().getZone()).isEqualTo("main");
        assertThat(result.getFirst().getSeats()).isEqualTo(4);
    }

    @Test
    void getAllTables_whenRangeInvalid_throwsValidationException() {
        LocalDateTime start = LocalDateTime.now().plusHours(3);
        LocalDateTime end = LocalDateTime.now().plusHours(1);

        assertThatThrownBy(() -> tableService.getAllTables(start, end))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Start time");
    }

    @Test
    void setTablePosition_whenTableExists_updatesPosition() {
        PositionDto positionDto = new PositionDto();
        positionDto.setX(300);
        positionDto.setY(400);

        when(tableRepository.changePosition(tableId, 300, 400)).thenReturn(tableEntity);

        tableService.setTablePosition(tableId, positionDto);
        verify(tableRepository, times(1)).changePosition(tableId, 300, 400);
    }

    @Test
    void setTablePosition_whenTableNotFound_throwsNotFoundException() {
        PositionDto positionDto = new PositionDto();
        positionDto.setX(300);
        positionDto.setY(400);

        when(tableRepository.changePosition(tableId, 300, 400)).thenReturn(null);

        assertThatThrownBy(() -> tableService.setTablePosition(tableId, positionDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Table not found");
    }
}

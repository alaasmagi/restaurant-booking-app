package com.alaasmagi.restaurant_booking_api.application;

import com.alaasmagi.restaurant_booking_api.application.contracts.IBookingRepository;
import com.alaasmagi.restaurant_booking_api.application.contracts.ITableRepository;
import com.alaasmagi.restaurant_booking_api.application.dtos.BookingDto;
import com.alaasmagi.restaurant_booking_api.application.dtos.CreateBookingDto;
import com.alaasmagi.restaurant_booking_api.application.exceptions.ConflictException;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private IBookingRepository bookingRepository;

    @Mock
    private ITableRepository tableRepository;

    @InjectMocks
    private BookingService bookingService;

    private UUID bookingId;
    private BookingEntity bookingEntity;
    private TableEntity tableEntity;

    @BeforeEach
    void setUp() {
        bookingId = UUID.randomUUID();
        bookingEntity = new BookingEntity();
        bookingEntity.setId(bookingId);
        bookingEntity.setTableId(UUID.randomUUID());
        bookingEntity.setStatus("active");
        bookingEntity.setCustomerName("Jane Doe");
        bookingEntity.setCustomerPhone("12345678");
        bookingEntity.setCustomerEmail("jane@example.com");
        bookingEntity.setPeopleCount(2);
        bookingEntity.setStartTime(LocalDateTime.now().plusHours(1));
        bookingEntity.setEndTime(LocalDateTime.now().plusHours(3));

        tableEntity = new TableEntity();
        tableEntity.setId(bookingEntity.getTableId());
        tableEntity.setSeats(4);
    }

    @Test
    void getBookingById_whenExists_returnsDto() {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(bookingEntity));

        Optional<BookingDto> result = bookingService.getBookingById(bookingId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(bookingId);
        assertThat(result.get().getCustomerName()).isEqualTo("Jane Doe");
        assertThat(result.get().getStatus()).isEqualTo("active");
        verify(bookingRepository, times(1)).findById(bookingId);
    }

    @Test
    void getBookingById_whenNotExists_returnsEmpty() {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        Optional<BookingDto> result = bookingService.getBookingById(bookingId);

        assertThat(result).isEmpty();
        verify(bookingRepository, times(1)).findById(bookingId);
    }

    @Test
    void getAllBookings_returnsListOfDtos() {
        when(bookingRepository.findAll()).thenReturn(List.of(bookingEntity));

        List<BookingDto> result = bookingService.getAllBookings();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(bookingId);
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void getAllBookings_whenEmpty_returnsEmptyList() {
        when(bookingRepository.findAll()).thenReturn(List.of());

        List<BookingDto> result = bookingService.getAllBookings();

        assertThat(result).isEmpty();
    }

    @Test
    void createBooking_savesEntityWithActiveStatus_andReturnsDto() {
        CreateBookingDto request = new CreateBookingDto();
        request.setTableId(bookingEntity.getTableId());
        request.setCustomerName("Jane Doe");
        request.setCustomerPhone("12345678");
        request.setCustomerEmail("jane@example.com");
        request.setPeopleCount(2);
        request.setStartTime(bookingEntity.getStartTime());
        request.setEndTime(bookingEntity.getEndTime());

        when(tableRepository.findById(bookingEntity.getTableId())).thenReturn(Optional.of(tableEntity));
        when(bookingRepository.findByTimestamps(request.getStartTime(), request.getEndTime())).thenReturn(List.of());
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(bookingEntity);

        BookingDto result = bookingService.createBooking(request);

        assertThat(result).isNotNull();
        assertThat(result.getCustomerName()).isEqualTo("Jane Doe");
        assertThat(result.getStatus()).isEqualTo("active");

        verify(bookingRepository).save(argThat(entity -> "active".equals(entity.getStatus())));
    }

    @Test
    void createBooking_whenTimeRangeInvalid_throwsValidationException() {
        CreateBookingDto request = new CreateBookingDto();
        request.setTableId(bookingEntity.getTableId());
        request.setCustomerName("Jane Doe");
        request.setCustomerPhone("12345678");
        request.setPeopleCount(2);
        request.setStartTime(LocalDateTime.now().plusHours(3));
        request.setEndTime(LocalDateTime.now().plusHours(1));

        assertThatThrownBy(() -> bookingService.createBooking(request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("start time");
    }

    @Test
    void createBooking_whenTableMissing_throwsNotFoundException() {
        CreateBookingDto request = new CreateBookingDto();
        request.setTableId(bookingEntity.getTableId());
        request.setCustomerName("Jane Doe");
        request.setCustomerPhone("12345678");
        request.setPeopleCount(2);
        request.setStartTime(bookingEntity.getStartTime());
        request.setEndTime(bookingEntity.getEndTime());

        when(tableRepository.findById(bookingEntity.getTableId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.createBooking(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Table not found");
    }

    @Test
    void createBooking_whenPeopleCountExceedsSeats_throwsValidationException() {
        CreateBookingDto request = new CreateBookingDto();
        request.setTableId(bookingEntity.getTableId());
        request.setCustomerName("Jane Doe");
        request.setCustomerPhone("12345678");
        request.setPeopleCount(5);
        request.setStartTime(bookingEntity.getStartTime());
        request.setEndTime(bookingEntity.getEndTime());

        when(tableRepository.findById(bookingEntity.getTableId())).thenReturn(Optional.of(tableEntity));

        assertThatThrownBy(() -> bookingService.createBooking(request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("seating capacity");
    }

    @Test
    void createBooking_whenOverlapExists_throwsConflictException() {
        CreateBookingDto request = new CreateBookingDto();
        request.setTableId(bookingEntity.getTableId());
        request.setCustomerName("Jane Doe");
        request.setCustomerPhone("12345678");
        request.setPeopleCount(2);
        request.setStartTime(bookingEntity.getStartTime());
        request.setEndTime(bookingEntity.getEndTime());

        when(tableRepository.findById(bookingEntity.getTableId())).thenReturn(Optional.of(tableEntity));
        when(bookingRepository.findByTimestamps(request.getStartTime(), request.getEndTime())).thenReturn(List.of(bookingEntity));

        assertThatThrownBy(() -> bookingService.createBooking(request))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("active booking");
    }

    @Test
    void cancelBooking_whenExists_setsStatusCancelled() {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(bookingEntity));
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(bookingEntity);

        bookingService.cancelBooking(bookingId);
        verify(bookingRepository).save(argThat(entity -> "cancelled".equals(entity.getStatus())));
    }

    @Test
    void cancelBooking_whenNotExists_throwsNotFoundException() {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.cancelBooking(bookingId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Booking not found");
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void cancelBooking_whenAlreadyCancelled_throwsConflictException() {
        bookingEntity.setStatus("cancelled");
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(bookingEntity));

        assertThatThrownBy(() -> bookingService.cancelBooking(bookingId))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("already cancelled");
        verify(bookingRepository, never()).save(any());
    }
}

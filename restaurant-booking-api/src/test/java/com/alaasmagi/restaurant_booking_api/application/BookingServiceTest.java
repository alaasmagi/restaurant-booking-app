package com.alaasmagi.restaurant_booking_api.application;

import com.alaasmagi.restaurant_booking_api.application.contracts.IBookingRepository;
import com.alaasmagi.restaurant_booking_api.application.dtos.BookingDto;
import com.alaasmagi.restaurant_booking_api.application.dtos.CreateBookingDto;
import com.alaasmagi.restaurant_booking_api.domain.BookingEntity;
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
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private IBookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    private UUID bookingId;
    private BookingEntity bookingEntity;

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
    }

    @Test
    void getBookingById_whenExists_returnsDto() throws ExecutionException, InterruptedException {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(bookingEntity));

        Optional<BookingDto> result = bookingService.getBookingById(bookingId).get();

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(bookingId);
        assertThat(result.get().getCustomerName()).isEqualTo("Jane Doe");
        assertThat(result.get().getStatus()).isEqualTo("active");
        verify(bookingRepository, times(1)).findById(bookingId);
    }

    @Test
    void getBookingById_whenNotExists_returnsEmpty() throws ExecutionException, InterruptedException {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        Optional<BookingDto> result = bookingService.getBookingById(bookingId).get();

        assertThat(result).isEmpty();
        verify(bookingRepository, times(1)).findById(bookingId);
    }

    @Test
    void getAllBookings_returnsListOfDtos() throws ExecutionException, InterruptedException {
        when(bookingRepository.findAll()).thenReturn(List.of(bookingEntity));

        List<BookingDto> result = bookingService.getAllBookings().get();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(bookingId);
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void getAllBookings_whenEmpty_returnsEmptyList() throws ExecutionException, InterruptedException {
        when(bookingRepository.findAll()).thenReturn(List.of());

        List<BookingDto> result = bookingService.getAllBookings().get();

        assertThat(result).isEmpty();
    }

    @Test
    void createBooking_savesEntityWithActiveStatus_andReturnsDto() throws ExecutionException, InterruptedException {
        CreateBookingDto request = new CreateBookingDto();
        request.setTableId(bookingEntity.getTableId());
        request.setCustomerName("Jane Doe");
        request.setCustomerPhone("12345678");
        request.setCustomerEmail("jane@example.com");
        request.setPeopleCount(2);
        request.setStartTime(bookingEntity.getStartTime());
        request.setEndTime(bookingEntity.getEndTime());

        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(bookingEntity);

        BookingDto result = bookingService.createBooking(request).get();

        assertThat(result).isNotNull();
        assertThat(result.getCustomerName()).isEqualTo("Jane Doe");
        assertThat(result.getStatus()).isEqualTo("active");

        verify(bookingRepository).save(argThat(entity -> "active".equals(entity.getStatus())));
    }

    @Test
    void cancelBooking_whenExists_setsStatusCancelledAndReturnsTrue() throws ExecutionException, InterruptedException {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(bookingEntity));
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(bookingEntity);

        Boolean result = bookingService.cancelBooking(bookingId).get();

        assertThat(result).isTrue();
        verify(bookingRepository).save(argThat(entity -> "cancelled".equals(entity.getStatus())));
    }

    @Test
    void cancelBooking_whenNotExists_returnsFalse() throws ExecutionException, InterruptedException {
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        Boolean result = bookingService.cancelBooking(bookingId).get();

        assertThat(result).isFalse();
        verify(bookingRepository, never()).save(any());
    }
}


package com.alaasmagi.restaurant_booking_api.domain;

import com.alaasmagi.restaurant_booking_api.domain.enums.EBookingStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingEntityTest {

    @Test
    void activate_setsStatusToActive() {
        BookingEntity booking = new BookingEntity();

        booking.activate();

        assertThat(booking.getStatus()).isEqualTo(EBookingStatus.ACTIVE);
    }

    @Test
    void cancel_updatesStatusAndCanBeCancelledState() {
        BookingEntity booking = new BookingEntity();
        booking.activate();

        booking.cancel();

        assertThat(booking.getStatus()).isEqualTo(EBookingStatus.CANCELLED);
        assertThat(booking.canBeCancelled()).isFalse();
    }

    @Test
    void overlaps_returnsTrueWhenRangesIntersect() {
        BookingEntity booking = new BookingEntity();
        booking.setStartTime(LocalDateTime.of(2026, 3, 13, 18, 0));
        booking.setEndTime(LocalDateTime.of(2026, 3, 13, 20, 0));

        assertThat(booking.overlaps(
                LocalDateTime.of(2026, 3, 13, 19, 0),
                LocalDateTime.of(2026, 3, 13, 21, 0)
        )).isTrue();
    }
}

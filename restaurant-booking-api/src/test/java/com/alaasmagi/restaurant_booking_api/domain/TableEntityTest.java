package com.alaasmagi.restaurant_booking_api.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TableEntityTest {

    @Test
    void canAccommodate_returnsTrueWhenPartyFits() {
        TableEntity table = new TableEntity();
        table.setSeats(4);

        assertThat(table.canAccommodate(4)).isTrue();
    }

    @Test
    void canAccommodate_returnsFalseWhenPartyExceedsSeats() {
        TableEntity table = new TableEntity();
        table.setSeats(4);

        assertThat(table.canAccommodate(5)).isFalse();
    }
}

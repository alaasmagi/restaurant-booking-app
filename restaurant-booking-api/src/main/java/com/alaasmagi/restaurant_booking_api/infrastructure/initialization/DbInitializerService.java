package com.alaasmagi.restaurant_booking_api.infrastructure.initialization;

import com.alaasmagi.restaurant_booking_api.application.contracts.IBookingRepository;
import com.alaasmagi.restaurant_booking_api.application.contracts.ITableRepository;
import com.alaasmagi.restaurant_booking_api.domain.BookingEntity;
import com.alaasmagi.restaurant_booking_api.domain.TableEntity;
import com.alaasmagi.restaurant_booking_api.domain.enums.EBookingStatus;
import com.alaasmagi.restaurant_booking_api.domain.enums.ESeatFeature;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

@RequiredArgsConstructor
@Component
public class DbInitializerService implements CommandLineRunner {
    private final ITableRepository tableRepository;
    private final IBookingRepository bookingRepository;

    private static final List<Integer> SEAT_OPTIONS = List.of(2, 4, 6, 8);
    private static final List<String> ZONES = List.of("A", "B", "C");
    private static final List<ESeatFeature> FEATURES = List.of(
            ESeatFeature.WINDOW,
            ESeatFeature.PRIVATE,
            ESeatFeature.KIDS_CORNER,
            ESeatFeature.ACCESSIBLE,
            ESeatFeature.OUTDOOR,
            ESeatFeature.BAR_SEATING
    );
    private static final int TABLE_COUNT = 15;
    private static final int MAX_POSITION = 10;

    @Override
    public void run(String @NonNull ... args) {
        // Initialize tables if empty
        if (tableRepository.findAll().isEmpty()) {
            Set<String> usedPositions = new HashSet<>();
            Random random = new Random();
            for (int i = 0; i < TABLE_COUNT; i++) {
                TableEntity table = new TableEntity();
                table.setSeats(SEAT_OPTIONS.get(random.nextInt(SEAT_OPTIONS.size())));
                table.setZone(ZONES.get(random.nextInt(ZONES.size())));
                // Random features, but not all
                int featureCount = 1 + random.nextInt(FEATURES.size() - 1);
                List<ESeatFeature> shuffled = new ArrayList<>(FEATURES);
                Collections.shuffle(shuffled, random);
                table.setFeatures(new ArrayList<>(shuffled.subList(0, featureCount)));
                // Unique position
                int x;
                int y;
                String positionKey;
                do {
                    x = random.nextInt(MAX_POSITION + 1);
                    y = random.nextInt(MAX_POSITION + 1);
                    positionKey = x + ":" + y;
                } while (usedPositions.contains(positionKey));
                usedPositions.add(positionKey);
                table.setX(x);
                table.setY(y);
                tableRepository.save(table);
            }
        }
        if (!bookingRepository.findAll().isEmpty()) {
            return;
        }
        List<TableEntity> tables = tableRepository.findAll();
        Collections.shuffle(tables);
        int bookingCount = 5 + new Random().nextInt(3); // 5-7 bookings
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        for (int i = 0; i < bookingCount && i < tables.size(); i++) {
            TableEntity table = tables.get(i);
            BookingEntity booking = new BookingEntity();
            booking.setTableId(table.getId());
            booking.setStatus(EBookingStatus.ACTIVE);
            booking.setCustomerName("Test Customer " + (i + 1));
            booking.setCustomerPhone("+1234567890");
            booking.setCustomerEmail("customer" + (i + 1) + "@example.com");
            booking.setPeopleCount(table.getSeats());
            booking.setPreferences(table.getFeatures());
            LocalTime start = now.plusHours(i + 1);
            LocalTime end = start.plusHours(2);
            booking.setStartTime(LocalDateTime.of(today, start));
            booking.setEndTime(LocalDateTime.of(today, end));
            bookingRepository.save(booking);
        }
    }
}

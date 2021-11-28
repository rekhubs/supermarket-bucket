package com.example.supermarket.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Test
    void bookSlot() {
        // invalid input
        assertFalse(bookingService.bookSlot(1, 10, 1));
        assertFalse(bookingService.bookSlot(1, -3, 1));
        assertFalse(bookingService.bookSlot(1, 2, 30));
        assertFalse(bookingService.bookSlot(1, 2, -4));

        // start booking
        assertTrue(bookingService.bookSlot(1, 2, 1));
        assertFalse(bookingService.bookSlot(1, 2, 1));
        for (int u = 2; u < 9; u++) {
            assertTrue(bookingService.bookSlot(u, 2, 1));
        }
        // slot full
        assertFalse(bookingService.bookSlot(20, 2, 1));
        // user 20 find another slot
        assertTrue(bookingService.bookSlot(20, 2, 8));
        // user 1 find another day
        assertTrue(bookingService.bookSlot(1, 6, 8));
        log.info("--> all {}", bookingService.getAll());
    }

    @Test
    void getByUserId() {
    }

    @Test
    void getByWeekday() {
    }

    @Test
    void getByUserIdAndWeekday() {
    }
}
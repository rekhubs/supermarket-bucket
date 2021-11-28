package com.example.supermarket.service;

import com.example.supermarket.model.Booking;
import com.example.supermarket.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingService {

    @Value("${max.users.per.slot}")
    private int MAX_USERS_PER_SLOT;

    @Value("${max.slots.per.day}")
    private int MAX_SLOTS_PER_DAY;

    @Value("${max.bookings.per.user.day}")
    private int MAX_BOOKINGS_PER_USER_PER_DAY;

    List<Booking> bookings = new ArrayList<>();

    BiPredicate<Booking, Integer> isBookedByUser = (Booking b, Integer uid) -> uid.intValue() == b.getUser().getId();

    // todo - tests - unit tests, integration test

    // tmp - todo remove
    public List<Booking> getAll() {
        return this.bookings;
    }

    /**
     *
     * @param userId user ID
     * @param weekday int value follows the ISO-8601 standard, from 1 (Monday) to 7 (Sunday)
     * @param slot hour of the day (0 - 23)
     * @return <code>true</code> if the booking is successful, otherwise <code>false</code>
     */
    public synchronized boolean bookSlot(int userId, int weekday, int slot) {
        if (slot < 0 || slot >= MAX_SLOTS_PER_DAY) {
            log.warn("invalid slot input: {}", slot);
            return false;
        }
        if (this.getByUserIdAndWeekday(userId, weekday).stream().count() >= MAX_BOOKINGS_PER_USER_PER_DAY) {
            log.warn("user {} has exceeded daily booking limit!", userId);
            return false;
        }

        List<Booking> bookingsInSlot = this.getByWeekday(weekday).stream()
            .filter(booking -> slot == booking.getSlot())
            .collect(Collectors.toList());
        if (bookingsInSlot.stream().count() >= MAX_USERS_PER_SLOT) {
            log.warn("slot {} on weekday {} is fully booked!", slot, weekday);
            return false;
        }

        if (bookingsInSlot.stream()
            .filter(booking -> userId == booking.getUser().getId())
            .findAny()
            .isPresent()
        ) {
            log.warn("user {} has booking on weekday {} slot {}", userId, weekday, slot);
            return false;
        }

        try {
            this.bookings.add(new Booking(weekday, slot, new User(userId)));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(),
                "failed to make a booking weekday={}, slot={}, for user={}.",
                weekday, slot, userId);
            return false;
        }
    }

    public List<Booking> getByUserId(int userId) {
        return this.bookings.stream()
            .filter(booking -> userId == booking.getUser().getId())
            .sorted(Comparator.comparing(Booking::getWeekday).thenComparing(Booking::getSlot))
            .collect(Collectors.toList());
    }

    public List<Booking> getByWeekday(int weekday) {
        return this.bookings.stream()
            .filter(booking -> weekday == booking.getWeekday())
            .sorted(Comparator.comparing(Booking::getSlot))  // todo - then sort by user name
            .collect(Collectors.toList());
    }

    public List<Booking> getByUserIdAndWeekday(int userId, int weekday) {
        return this.bookings.stream()
            .filter(booking -> (weekday == booking.getWeekday() && userId == booking.getUser().getId()))
            .sorted(Comparator.comparing(Booking::getSlot))
            .collect(Collectors.toList());
    }

}

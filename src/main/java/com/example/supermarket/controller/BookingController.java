package com.example.supermarket.controller;

import com.example.supermarket.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("api/v1")
@RestController
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // tmp for util, todo - remove
    @GetMapping("/bookings")
    public ResponseEntity getAll() {
        return ResponseEntity.ok(bookingService.getAll());
    }

    @PostMapping("/bookings")
    public ResponseEntity book(
        @RequestParam("userId") int userId,
        @RequestParam("weekday") int weekday,
        @RequestParam("slot") int slot
    ) {
        log.info("----> booking req: u={}, day={}, s={}", userId, weekday, slot);
        if (bookingService.bookSlot(userId, weekday, slot)) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @GetMapping("/users/{uid}/bookings")
    public ResponseEntity getByUserId(@PathVariable int uid) {
        return ResponseEntity.ok(bookingService.getByUserId(uid));
    }

    @GetMapping("/bookings/agg/weekday/{day}")
    public ResponseEntity getByWeekday(@PathVariable int day) {
        return ResponseEntity.ok(bookingService.getByWeekday(day));
    }

}


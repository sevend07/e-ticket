package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.request.CreateBookingRequest;
import com.example.demo.DTO.response.BookingResponse;
import com.example.demo.DTO.response.Response;
import com.example.demo.service.BookingService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/booking")
public class BookingController {
    private final BookingService service;

    @GetMapping
    public ResponseEntity<?> getBookings() {
        List<BookingResponse> datas = service.findAll();

        return ResponseEntity.ok(Response.generateResponse(
                "Bookings Found", datas));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getDetail(@PathVariable Integer id) {
        BookingResponse data = service.findById(id);

        return ResponseEntity.ok(Response.generateResponse(
                "Booking Found", data));
    }

    @PostMapping("create")
    public ResponseEntity<?> create(CreateBookingRequest request) {
        BookingResponse data = service.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Response.generateResponse(
                        "Booking Created Successfully", data));
    }

    @GetMapping("bookings/{userId}")
    public ResponseEntity<?> getBookingByUser(@PathVariable Integer userId) {
        List<BookingResponse> datas = service.findBookingByUserId(userId);

        return ResponseEntity.ok(Response.generateResponse(
                "Bookings Found", datas));
    }
}

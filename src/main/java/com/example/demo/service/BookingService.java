package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.request.CreateBookingRequest;
import com.example.demo.DTO.response.BookingResponse;
import com.example.demo.enums.BookingStatus;
import com.example.demo.mapper.BookingMapper;
import com.example.demo.model.Booking;
import com.example.demo.model.BookingItem;
import com.example.demo.model.Seat;
import com.example.demo.model.Trip;
import com.example.demo.model.User;
import com.example.demo.repository.BookingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository repo;
    private final SeatService seatService;
    private final TripService tripService;
    private final UserService userService;

    public BookingResponse create(CreateBookingRequest request) {
        Trip trip = tripService.getTripById(request.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip Not Found"));

        User customer = userService.getUserById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        List<Seat> availableSeats = seatService.getAvailableSeatByTrip(
                trip.getId(), trip.getFleet().getType().getId());

        if (request.getPassengerCount() > availableSeats.size())
            throw new RuntimeException("Seat not enough");

        Booking booking = new Booking();
        booking.setTrip(trip);
        booking.setUser(customer);
        booking.setStatus(BookingStatus.BOOKED);
        booking.setTotalAmount(countTotalAmount(availableSeats));

        List<BookingItem> bookingItems = new ArrayList<>();

        for (int i = 1; i <= request.getPassengerCount(); i++) {
            BookingItem item = new BookingItem();
            item.setPassengerName(request.getPassengerNames().get(i));
            item.setBooking(booking);
            item.setSeat(availableSeats.get(i));

            bookingItems.add(item);
        }

        booking.setBookingItems(bookingItems);

        Booking createdBooking = repo.save(booking);

        return BookingMapper.toResponse(createdBooking);
    }

    private Integer countTotalAmount(List<Seat> seats) {
        return seats.stream()
                .mapToInt(seat -> seat.getType().getPrice())
                .sum();
    }
}

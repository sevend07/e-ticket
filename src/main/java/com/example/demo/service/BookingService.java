package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.request.CreateBookingRequest;
import com.example.demo.DTO.response.BookingResponse;
import com.example.demo.enums.BookingStatus;
import com.example.demo.exception.BusinessException;
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

    public BookingResponse findById(Integer id) {
        return repo.findById(id).map(BookingMapper::includeItem)
                .orElseThrow(() -> new BusinessException(
                        "Booking Not Found", HttpStatus.NOT_FOUND));
    }

    public List<BookingResponse> findAll() {
        return repo.findAll().stream().map(BookingMapper::summary).toList();
    }

    public List<BookingResponse> findBookingByUserId(Integer userId) {
        return repo.findByUserId(userId).stream().map(BookingMapper::summary).toList();
    }

    @Transactional
    public BookingResponse create(CreateBookingRequest request) {
        Trip trip = tripService.findById(request.getTripId())
                .orElseThrow(() -> new BusinessException(
                        String.format("Trip With id %s Not Found", request.getTripId()),
                        HttpStatus.NOT_FOUND));

        User customer = userService.getUserById(request.getCustomerId())
                .orElseThrow(() -> new BusinessException(
                        String.format("User With id %s Not Found", request.getCustomerId()),
                        HttpStatus.NOT_FOUND));

        List<Seat> availableSeats = seatService.getAvailableSeatByTrip(
                trip.getId(), trip.getFleet().getType().getId());

        if (request.getPassengerCount() > availableSeats.size())
            throw new BusinessException("Seat not enough", HttpStatus.BAD_REQUEST);

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

        return BookingMapper.includeItem(createdBooking);
    }

    private Integer countTotalAmount(List<Seat> seats) {
        return seats.stream()
                .mapToInt(seat -> seat.getType().getPrice())
                .sum();
    }
}

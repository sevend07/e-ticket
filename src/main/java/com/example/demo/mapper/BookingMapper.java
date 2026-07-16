package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.DTO.response.BookingResponse;
import com.example.demo.DTO.response.TripResponse;
import com.example.demo.model.Booking;
import com.example.demo.model.BookingItem;
import com.example.demo.model.Fleet;
import com.example.demo.model.Seat;
import com.example.demo.model.Trip;
import com.example.demo.model.Type;

@Component
public class BookingMapper {
    public static BookingResponse plain(Booking b) {
        if (b == null)
            return null;

        return new BookingResponse(
                b.getId(),
                b.getStatus(),
                null,
                b.getTotalAmount(),
                b.getBookingItems() != null ? b.getBookingItems().size() : 0,
                null);
    }

    public static BookingResponse summary(Booking b) {
        if (b == null)
            return null;

        Trip trip = b.getTrip();
        TripResponse.CompleteResponse tripSummary = TripMapper.summary(trip);
        return new BookingResponse(
                b.getId(),
                b.getStatus(),
                tripSummary,
                b.getTotalAmount(),
                b.getBookingItems().size(),
                null);
    }

    public static BookingResponse includeItem(Booking b) {
        if (b == null)
            return null;

        Trip trip = b.getTrip();
        TripResponse.CompleteResponse tripSummary = TripMapper.summary(trip);
        return new BookingResponse(
                b.getId(),
                b.getStatus(),
                tripSummary,
                b.getTotalAmount(),
                b.getBookingItems().size(),
                b.getBookingItems().stream()
                        .map(BookingMapper::toBookingItemResponse)
                        .toList());
    }

    public static BookingResponse.BookingItemResponse toBookingItemResponse(BookingItem items) {
        Fleet fleet = items.getBooking().getTrip().getFleet();
        Seat seat = items.getSeat();
        Type type = items.getSeat().getType();

        return new BookingResponse.BookingItemResponse(
                items.getId(),
                fleet.getCode(),
                items.getPassengerName(),
                seat.getCode(),
                type.getPrice());
    }
}

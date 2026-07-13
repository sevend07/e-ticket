package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.DTO.response.BookingResponse;
import com.example.demo.model.Booking;
import com.example.demo.model.BookingItem;
import com.example.demo.model.Fleet;
import com.example.demo.model.Seat;
import com.example.demo.model.Trip;
import com.example.demo.model.Type;

@Component
public class BookingMapper {
    public static BookingResponse toResponse(Booking b) {
        return new BookingResponse(
                b.getId(),
                b.getStatus(),
                b.getTotalAmount(),
                b.getTrip().getId(),
                b.getBookingItems().stream()
                        .map(BookingMapper::toBookingItemResponse)
                        .toList());
    }

    public static BookingResponse.BookingItem toBookingItemResponse(BookingItem items) {
        Trip trip = items.getBooking().getTrip();
        Fleet fleet = items.getBooking().getTrip().getFleet();
        Seat seat = items.getSeat();
        Type type = items.getSeat().getType();

        return new BookingResponse.BookingItem(
            items.getId(),
            items.getPassengerName(),
            trip.getDepartureTerminal().getName(),
            trip.getDestinationTerminal().getName(),
            fleet.getType().getType(),
            fleet.getCode(),
            seat.getCode(),
            type.getPrice()
        );
    }
}

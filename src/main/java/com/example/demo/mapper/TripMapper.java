package com.example.demo.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.DTO.response.BookingResponse;
import com.example.demo.DTO.response.TripResponse;
import com.example.demo.model.Trip;

@Component
public class TripMapper {
    public static TripResponse.CompleteResponse summary(Trip t) {
        if (t == null)
            return null;

        return new TripResponse.CompleteResponse(
                t.getId(),
                t.getDepartureTime(),
                t.getArrivalTime(),
                t.getDepartureTerminal().getName(),
                t.getDestinationTerminal().getName(),
                t.getDepartureTerminal().getCity(),
                t.getDestinationTerminal().getCity(),
                t.getFleet().getBus().getName(),
                t.getFleet().getCode(),
                t.getFleet().getType().getType(),
                t.getFleet().getType().getPrice());
    }

    public static TripResponse.DetailResponse detail(Trip t,
            List<TripResponse.SeatInformation> seatInformations) {
        if (t == null)
            return null;

        List<BookingResponse> bookingSummaries = t.getBookings().stream()
                .map(BookingMapper::plain).toList();

        return new TripResponse.DetailResponse(
                summary(t),
                bookingSummaries,
                seatInformations);
    }

}

package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.DTO.response.TripResponse;
import com.example.demo.model.Trip;

@Component
public class TripMapper {
    public TripResponse.CompleteResponse toCompleteResponse(Trip t) {
        return new TripResponse.CompleteResponse(
                t.getId(),
                t.getDepartureTime(),
                t.getArrivalTime(),
                t.getDepartureTerminal().getName(),
                t.getDestinationTerminal().getName(),
                t.getDepartureTerminal().getCity(),
                t.getDestinationTerminal().getCity(),
                t.getFleet().getBus().getName(),
                t.getFleet().getType().getType(),
                t.getFleet().getType().getPrice());
    }
}

package com.example.demo.DTO.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TripRequestDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FindAvailableTrips {
        private Integer departureTerminalId, destinationTerminalId,
                passengerCount;
        private LocalDate schedule;
    }
}

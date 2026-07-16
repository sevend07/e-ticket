package com.example.demo.DTO.response;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.enums.Types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TripResponse {
    @Data
    @AllArgsConstructor
    public static class DetailResponse {
        private CompleteResponse tripSummary;
        private List<BookingResponse> bookingSummary;
        private List<SeatInformation> seatInformations;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompleteResponse {
        private Integer id;
        private LocalDateTime departureTime, arrivalTime;
        private String departureTerminal, destinationTerminal;
        private String departureTerminalCity, destinationTerminalCity;
        private String busName, busCode;
        private Types busType;
        private Integer price;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SeatInformation {
        private Integer id;
        private String seatCode;
        private Boolean booked;
    }

}

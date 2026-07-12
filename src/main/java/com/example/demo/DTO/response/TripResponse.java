package com.example.demo.DTO.response;

import java.time.LocalDateTime;

import com.example.demo.enums.Types;

import lombok.AllArgsConstructor;
import lombok.Data;

public class TripResponse {
    @Data
    @AllArgsConstructor
    public static class CompleteResponse {
        private Integer id;
        private LocalDateTime departureTime, arrivalTime;
        private String departureTerminal, destinationTerminal;
        private String departureTerminalCity, destinationTerminalCity;
        private String busName;
        private Types busType;
        private Integer price;
    }
}

package com.example.demo.DTO.response;

import java.util.List;

import com.example.demo.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingResponse {
    private Integer id;
    private BookingStatus status;
    private TripResponse.CompleteResponse tripSummary;
    private Integer totalAmount, totalPassenger;
    private List<BookingItemResponse> bookingItems;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookingItemResponse {
        private Integer bookingItemId;
        private String passengerName;
        private String fleetCode;
        private String seatCode;
        private Integer price;
    }
}

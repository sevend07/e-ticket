package com.example.demo.DTO.response;

import java.util.List;

import com.example.demo.enums.BookingStatus;
import com.example.demo.enums.Types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Integer id;
    private BookingStatus status;
    private Integer totalAmount;
    private Integer tripId;
    private List<BookingItem> BookingItems;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookingItem {
        private Integer bookingItemId;
        private String passengerName;
        private String departureTerminal, destinationTerminal;
        private Types fleetType;
        private String fleetCode;
        private String seatCode;
        private Integer price;
    }

}

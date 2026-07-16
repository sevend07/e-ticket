package com.example.demo.DTO.response;

import com.example.demo.DTO.response.BookingResponse.BookingItemResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponse {
    private Integer id;
    private BookingItemResponse bookingItem;
    private String qrCode;
}

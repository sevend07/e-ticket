package com.example.demo.DTO.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponse {
    private Integer id;
    private BookingResponse.BookingItem bookingItem;
    private String qrCode;
}

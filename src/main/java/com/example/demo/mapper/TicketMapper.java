package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.DTO.response.TicketResponse;
import com.example.demo.model.Ticket;

@Component
public class TicketMapper {
    public static TicketResponse toResponse(Ticket t) {
        return new TicketResponse(
            t.getId(),
            BookingMapper.toBookingItemResponse(t.getBookingItem()),
            t.getQrCode()
        );
    }
}

package com.example.demo.DTO.response;

import java.util.List;

public record BusResponseDto(
        Integer id,
        String name,
        List<FleetResponseDto> fleets) {
}

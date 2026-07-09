package com.example.demo.DTO.response;

import com.example.demo.enums.Types;

public record FleetResponseDto(
    Integer id,
    String code,
    Integer typeId,
    Types type
) {
}

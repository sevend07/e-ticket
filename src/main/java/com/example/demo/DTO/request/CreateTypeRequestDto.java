package com.example.demo.DTO.request;

import com.example.demo.enums.Types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTypeRequestDto {
    private Types type;
    private Integer totalSeat, price;
}

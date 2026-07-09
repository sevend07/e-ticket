package com.example.demo.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateFleetRequestDto {
    private Integer busId, typeId;
    private Integer quantity;
}

package com.example.demo.DTO.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBusRequestDto {
    private String name;
    List<CreateFleetRequestDto> fleets;
}

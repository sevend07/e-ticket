package com.example.demo.DTO.response;

import java.util.List;

import com.example.demo.enums.Types;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FleetResponseDto {
    private Integer id;
    private String code;
    private Integer typeId;
    private Types type;
    private List<TripResponse.CompleteResponse> trips;
}

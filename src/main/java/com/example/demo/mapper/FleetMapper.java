package com.example.demo.mapper;

import java.util.List;

import com.example.demo.DTO.response.FleetResponseDto;
import com.example.demo.DTO.response.TripResponse;
import com.example.demo.model.Fleet;

public class FleetMapper {
    public static FleetResponseDto plain(Fleet f) {
        return FleetResponseDto.builder()
                .id(f.getId())
                .code(f.getCode())
                .typeId(f.getId())
                .type(f.getType().getType())
                .trips(null)
                .build();
    }

    public static FleetResponseDto includeTrips(Fleet f) {
        List<TripResponse.CompleteResponse> trips = f.getTrips().stream()
                .map(TripMapper::summary).toList();
        return FleetResponseDto.builder()
                .id(f.getId())
                .code(f.getCode())
                .typeId(f.getId())
                .type(f.getType().getType())
                .trips(trips)
                .build();
    }
}

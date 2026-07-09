package com.example.demo.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.demo.DTO.request.CreateBusRequestDto;
import com.example.demo.DTO.request.CreateFleetRequestDto;
import com.example.demo.DTO.response.BusResponseDto;
import com.example.demo.model.Bus;
import com.example.demo.repository.BusRepository;
import com.example.demo.repository.FleetRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BusService {
    private final BusRepository busRepo;
    private final FleetRepository fleetRepo;

    public List<BusResponseDto> bulkCreateBus(List<CreateBusRequestDto> requests) {
        for (CreateBusRequestDto request : requests) {
            Bus bus = new Bus();
            bus.setName(request.getName());
        }
    }

    public List<FleetResponseDto> createFleet(List<CreateFleetRequestDto> requests) {
        Map<Integer, CreateFleetRequestDto> request = requests.stream()
                .collect(Collectors.toMap(r -> r.getTypeId(), r -> r));
        
        
    }
}

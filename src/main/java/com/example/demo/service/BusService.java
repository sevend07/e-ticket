package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.request.CreateBusRequestDto;
import com.example.demo.DTO.request.CreateFleetRequestDto;
import com.example.demo.DTO.response.BusResponseDto;
import com.example.demo.DTO.response.FleetResponseDto;
import com.example.demo.model.Bus;
import com.example.demo.model.Fleet;
import com.example.demo.model.Type;
import com.example.demo.repository.BusRepository;
import com.example.demo.repository.FleetRepository;
import com.example.demo.repository.TypeRepository;
import com.example.demo.util.CodeGenerationUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BusService {
    private final BusRepository busRepo;
    private final FleetRepository fleetRepo;
    private final TypeRepository typeRepo;

    @Transactional
    public List<BusResponseDto> bulkCreateBus(List<CreateBusRequestDto> requests) {
        List<Bus> newBuses = new ArrayList<>();

        for (CreateBusRequestDto request : requests) {
            Bus bus = new Bus();
            bus.setName(request.getName());
            bus.setFleets(createFleet(bus, request.getFleets()));

            newBuses.add(bus);
        }

        List<Bus> createdBuses = busRepo.saveAll(newBuses);

        List<BusResponseDto> response = new ArrayList<>();

        for (Bus b : createdBuses) {
            List<FleetResponseDto> fleetResponses = b.getFleets().stream()
                    .map(f -> new FleetResponseDto(
                            f.getId(), f.getCode(), f.getType().getId(),
                            f.getType().getType()))
                    .toList();

            response.add(new BusResponseDto(b.getId(), b.getName(), fleetResponses));
        }

        return response;
    }

    public List<Fleet> createFleet(Bus bus, List<CreateFleetRequestDto> requests) {

        try {
            Map<Integer, Integer> requestMap = requests.stream()
                    .collect(Collectors.toMap(r -> r.getTypeId(), r -> r.getQuantity()));

            Map<Integer, Type> typeMap = requestMap.keySet().stream()
                    .collect(Collectors.toMap(id -> id, id -> typeRepo.findById(id)
                            .orElseThrow(() -> new RuntimeException("Type Not Found"))));

            List<Fleet> newFleets = new ArrayList<>();

            CodeGenerationUtil fleetCodeGenerator = new CodeGenerationUtil(fleetRepo);
            requestMap.forEach((key, value) -> {
                for (Integer i = 1; i <= value; i++) {
                    Fleet fleet = new Fleet();
                    fleet.setBus(bus);
                    fleet.setCode(fleetCodeGenerator.generateFleetCode(bus));
                    fleet.setType(typeMap.get(key));

                    newFleets.add(fleet);
                }
            });

            return newFleets;
        } catch (Exception e) {
            throw new RuntimeException(String.format("Error: %s", e.getMessage()));
        }

    }
}

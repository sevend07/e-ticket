package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.demo.model.Fleet;
import com.example.demo.repository.FleetRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FleetService {
    private final FleetRepository repo;

    public Fleet findById(Integer id) {
        return repo.findById(id).orElseThrow(
                () -> new RuntimeException("Fleet not found"));
    }

    public List<Fleet> findAllById(Set<Integer> fleetIds) {
        return repo.findAllById(fleetIds);
    }

    public List<Fleet> findAvailableFleetByBusAndSchedule(
            Integer busId, LocalDateTime departure, LocalDateTime arrival) {
        return repo.findAvailableFleetByBusAndSchedule(busId, departure, arrival);
    }
}

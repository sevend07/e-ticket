package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Seat;
import com.example.demo.repository.SeatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository repo;

    List<Seat> getAvailableSeatByTrip(Integer tripId, Integer typeId) {
        return repo.findAvailableSeatByTrip(tripId, typeId);
    }
}

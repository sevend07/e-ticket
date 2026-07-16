package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.response.TripResponse.SeatInformation;
import com.example.demo.model.Seat;
import com.example.demo.repository.SeatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository repo;

    public List<Seat> getAvailableSeatByTrip(Integer tripId, Integer typeId) {
        return repo.findAvailableSeatByTrip(tripId, typeId);
    }

    public List<SeatInformation> getSeatInformationByTrip(Integer tripId, Integer typeId) {
        return repo.findSeatInformationByTrip(tripId, typeId);
    }
}

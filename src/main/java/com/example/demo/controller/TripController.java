package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.request.CreateTripRequest;
import com.example.demo.DTO.request.TripRequestDto;
import com.example.demo.DTO.response.Response;
import com.example.demo.DTO.response.TripResponse;
import com.example.demo.service.TripService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/trip")
@RequiredArgsConstructor
public class TripController {
    private final TripService service;

    @GetMapping("{id}")
    public ResponseEntity<?> getDetail(@PathVariable Integer id) {
        TripResponse.DetailResponse data = service.tripDetail(id);

        return ResponseEntity.ok(Response.generateResponse("Trip Detail", data));
    }

    @GetMapping("search")
    public ResponseEntity<?> searchAvailableTrips(@RequestBody TripRequestDto.FindAvailableTrips request) {
        List<TripResponse.CompleteResponse> datas = service.searchAvailableTrips(request);

        return ResponseEntity.ok(Response.generateResponse("Trips found", datas));
    }

    @PostMapping("create")
    public ResponseEntity<?> create(@RequestBody CreateTripRequest request) {
        List<TripResponse.CompleteResponse> datas = service.bulkCreate(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.generateResponse("Trip Created Successfully", datas));
    }
}

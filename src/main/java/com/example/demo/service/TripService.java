package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.request.CreateTripRequest;
import com.example.demo.DTO.request.CreateTripRequest.FleetScheduleRequest;
import com.example.demo.DTO.response.TripResponse;
import com.example.demo.mapper.TripMapper;
import com.example.demo.model.Fleet;
import com.example.demo.model.Terminal;
import com.example.demo.model.Trip;
import com.example.demo.repository.TripRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripMapper tripMapper;
    private final TripRepository repo;
    private final FleetService fleetService;
    private final TerminalService terminalService;

    public Optional<Trip> getTripById(Integer id) {
        return repo.findById(id);
    }

    @Transactional
    public List<TripResponse.CompleteResponse> GetAvailableTrip(String departureTerminal,
            String destinationTerminal,
            LocalDate schedule) {
        LocalDateTime start = schedule.atStartOfDay();
        LocalDateTime end = schedule.atTime(LocalTime.MAX);

        return repo.findTrips(departureTerminal, destinationTerminal, start, end);
    }

    // Overlap Schedule Validation
    public void validateOverlap(
            List<Trip> existingTrips, LocalDateTime outboundDeparture,
            LocalDateTime returnArrival, Integer fleetId, Map<Integer, Fleet> fleets) {
        for (Trip trip : existingTrips) {
            if (outboundDeparture.isBefore(trip.getArrivalTime())
                    && returnArrival.isAfter(trip.getDepartureTime())) {
                throw new RuntimeException(
                        String.format("Fleet %s has active schedule on %s - %s",
                                fleets.get(fleetId).getCode(), trip.getDepartureTime(),
                                trip.getArrivalTime()));
            }
        }
    }

    @Transactional
    public List<TripResponse.CompleteResponse> bulkCreate(CreateTripRequest request) {
        LocalDateTime outboundDeparture, outboundArrival,
                returnDeparture, returnArrival;
        Integer tripDuration = request.getEstimatedDuration();
        Integer turnaroundBuffer = request.getTurnaroundBufferDuration();

        // Time window to get trip history by fleet and schedule from request
        LocalDateTime minOutboundDeparture = request.getScheduledFleets().stream()
                .map(FleetScheduleRequest::getDepartureSchedule)
                .min(LocalDateTime::compareTo)
                .orElseThrow(() -> new RuntimeException("Fleet & Departure time request is empty"));
        LocalDateTime maxReturnArrival = request.getScheduledFleets().stream()
                .map(f -> f.getDepartureSchedule()
                        .plusHours(Integer.sum(tripDuration, turnaroundBuffer)))
                .max(LocalDateTime::compareTo)
                .orElseThrow(() -> new RuntimeException("Fleet & Departure time request is empty"));

        // Get Terminal 
        Terminal outboundDepartureTerminal = terminalService
                .findById(request.getDepartureTerminalId());
        Terminal outboundDestinationTerminal = terminalService
                .findById(request.getDestinationTerminalId());

        // Mapping outside for loop to avoid to much nested loop
        Map<Integer, FleetScheduleRequest> fleetSchedule = request.getScheduledFleets()
                .stream().collect(Collectors.toMap(r -> r.getFleetId(), r -> r));
        Map<Integer, Fleet> fleetMap = fleetService.findAllById(fleetSchedule.keySet())
                .stream().collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));

        // Querying trip hystory at once to avoid N+1
        Map<Integer, List<Trip>> tripHistoryByFleet = repo.findTripByTimeWindow(
                fleetMap.keySet(), minOutboundDeparture, maxReturnArrival)
                .stream().collect(Collectors.groupingBy(t -> t.getFleet().getId()));

        List<Trip> tripsToSave = new ArrayList<>();

        for (FleetScheduleRequest req : request.getScheduledFleets()) {
            outboundDeparture = req.getDepartureSchedule();
            outboundArrival = req.getDepartureSchedule().plusHours(tripDuration);
            returnDeparture = outboundArrival.plusHours(turnaroundBuffer);
            returnArrival = returnDeparture.plusHours(tripDuration);

            validateOverlap(
                    tripHistoryByFleet.getOrDefault(req.getFleetId(), List.of()),
                    outboundDeparture, returnArrival,
                    req.getFleetId(), fleetMap);

            Trip outboundTrip = new Trip();
            outboundTrip.setDepartureTime(outboundDeparture);
            outboundTrip.setArrivalTime(outboundArrival);
            outboundTrip.setDepartureTerminal(outboundDepartureTerminal);
            outboundTrip.setDestinationTerminal(outboundDestinationTerminal);
            outboundTrip.setFleet(fleetMap.get(req.getFleetId()));

            Trip returnTrip = new Trip();
            returnTrip.setDepartureTime(returnDeparture);
            returnTrip.setArrivalTime(returnArrival);
            returnTrip.setDepartureTerminal(outboundDestinationTerminal);
            returnTrip.setDestinationTerminal(outboundDepartureTerminal);
            returnTrip.setFleet(fleetMap.get(req.getFleetId()));

            tripsToSave.addAll(List.of(outboundTrip, returnTrip));

            // Add new trip to trip history for overlap validation againts new trip
            tripHistoryByFleet.computeIfAbsent(
                    req.getFleetId(), k -> new ArrayList<>())
                    .addAll(List.of(outboundTrip, returnTrip));
        }

        List<Trip> createdTrips = repo.saveAll(tripsToSave);

        return createdTrips.stream().map(tripMapper::toCompleteResponse)
                .toList();
    }
}

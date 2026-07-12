package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Transactional
    public List<TripResponse.CompleteResponse> GetAvailableTrip(String departureTerminal, String destinationTerminal,
            LocalDate schedule) {
        LocalDateTime start = schedule.atStartOfDay();
        LocalDateTime end = schedule.atTime(LocalTime.MAX);

        return repo.findTrips(departureTerminal, destinationTerminal, start, end);
    }

    public List<TripResponse.CompleteResponse> bulkCreate(CreateTripRequest request) {
        Terminal departureTerminal = terminalService.findById(request.getDepartureTerminalId());
        Terminal destinationTerminal = terminalService.findById(request.getDestinationTerminalId());

        // Set of selected fleet -> Ids
        Set<Integer> fleetIds = request.getScheduledFleets().stream()
                .flatMap(s -> s.getFleetIds().stream()).collect(Collectors.toSet());

        // Departute time paling awal dalam satu proses create trip
        LocalDateTime minDeparture = request.getScheduledFleets().stream()
                .map(FleetScheduleRequest::getDeparture)
                .min(LocalDateTime::compareTo)
                .orElseThrow(() -> new RuntimeException("Departure time is required"));

        // Arrival time paling akhir dalam satu proses create trip
        LocalDateTime maxArrival = request.getScheduledFleets().stream()
                .map(FleetScheduleRequest::getDeparture)
                .max(LocalDateTime::compareTo)
                .orElseThrow(() -> new RuntimeException("Arrival time is required"));

        // Existing Trip pada rentang waktu minDeparture hingga maxArrival
        List<Trip> conflictingTrips = repo.findConflictingTrips(fleetIds, minDeparture, maxArrival);

        // Grouping Existing Trip by fleetId untuk menghindari N+1
        Map<Integer, List<Trip>> conflictingTripsMap = conflictingTrips.stream()
                .collect(Collectors.groupingBy(t -> t.getFleet().getId()));

        // List Fleet yang natinya akan di attach (setFleet) saat membuat data Trip
        List<Fleet> fleets = fleetService.findAllById(fleetIds);

        // Map Fleet dengan key fleetId untuk menghindari N+1 
        Map<Integer, Fleet> fleetsMap = fleets.stream()
                .collect(Collectors.toMap(Fleet::getId, f -> f, (a, b) -> a));

        // List untuk menampung trip2 baru
        List<Trip> createdTrips = new ArrayList<>();

        for (FleetScheduleRequest newFleetSchedule : request.getScheduledFleets()) {
            LocalDateTime departureTime = newFleetSchedule.getDeparture();
            LocalDateTime arrivalTime = newFleetSchedule.getArrival();

            // validasi departure harus sebelum arrival
            if (arrivalTime.isBefore(departureTime))
                throw new RuntimeException("Arrival time must be later then departure time");

            // for loop di fleet2 yang di pilih 
            // jumlah trip yang terbuat tergantung berapa banyak fleet yang di pilih
            for (Integer fleetId : newFleetSchedule.getFleetIds()) {
                List<Trip> existingTrips = conflictingTripsMap.getOrDefault(fleetId, List.of());

                // Overlap schedule validation
                for (Trip existing : existingTrips) {
                    if (existing.getDepartureTime().isBefore(arrivalTime)
                            && existing.getArrivalTime().isAfter(departureTime)) {
                        throw new RuntimeException(
                                String.format("Fleet %s has active schedule on %s - %s",
                                        fleetsMap.get(fleetId).getCode(), existing.getDepartureTime(),
                                        existing.getArrivalTime()));

                    }
                }

                Trip trip = new Trip();
                trip.setDepartureTerminal(departureTerminal);
                trip.setDestinationTerminal(destinationTerminal);
                trip.setDepartureTime(departureTime);
                trip.setArrivalTime(arrivalTime);
                trip.setFleet(fleetsMap.get(fleetId));

                createdTrips.add(trip);

                // menambahkan trip baru ke Map existingTrip
                // untuk validasi untuk mencegah overlap antar trip baru 
                conflictingTripsMap.computeIfAbsent(fleetId, k -> new ArrayList<>())
                        .add(trip);

            }
        }

        repo.saveAll(createdTrips);

        return createdTrips.stream()
                .map(tripMapper::toCompleteResponse)
                .toList();
    }

}

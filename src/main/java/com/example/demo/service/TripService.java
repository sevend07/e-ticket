package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.request.CreateTripRequest;
import com.example.demo.DTO.request.CreateTripRequest.FleetScheduleRequest;
import com.example.demo.DTO.request.TripRequestDto;
import com.example.demo.DTO.response.TripResponse;
import com.example.demo.DTO.response.TripResponse.SeatInformation;
import com.example.demo.exception.BusinessException;
import com.example.demo.mapper.TripMapper;
import com.example.demo.model.Fleet;
import com.example.demo.model.Terminal;
import com.example.demo.model.Trip;
import com.example.demo.repository.TripRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripService {
	private final TripRepository repo;
	private final FleetService fleetService;
	private final TerminalService terminalService;
	private final SeatService seatService;

	@Transactional
	public TripResponse.DetailResponse tripDetail(Integer id) {
		Trip trip = repo.findById(id)
				.orElseThrow(() -> new BusinessException(
						String.format("Trip with id %s not found", id),
						HttpStatus.NOT_FOUND));

		List<SeatInformation> seatInfo = seatService.getSeatInformationByTrip(
				trip.getId(), trip.getFleet().getType().getId());

		return TripMapper.detail(trip, seatInfo);
	}

	public Optional<Trip> findById(Integer id) {
		return repo.findById(id);
	}

	@Transactional
	public List<TripResponse.CompleteResponse> searchAvailableTrips(TripRequestDto.FindAvailableTrips request) {
		LocalDateTime start = request.getSchedule().atStartOfDay();
		LocalDateTime end = request.getSchedule().atTime(LocalTime.MAX);

		List<TripResponse.CompleteResponse> results = repo.findAvailableTrips(
				request.getDepartureTerminalId(), request.getDestinationTerminalId(),
				request.getPassengerCount(), start, end);

		if (results.isEmpty()) {
			throw new BusinessException("There are no bus available for this trip", HttpStatus.NOT_FOUND);
		}

		return results;
	}

	// Overlap Schedule Validation
	public void validateOverlap(
			List<Trip> existingTrips, LocalDateTime outboundDeparture,
			LocalDateTime returnArrival, Integer fleetId, Map<Integer, Fleet> fleets) {
		for (Trip trip : existingTrips) {
			if (outboundDeparture.isBefore(trip.getArrivalTime())
					&& returnArrival.isAfter(trip.getDepartureTime())) {
				throw new BusinessException(
						String.format("Fleet %s has active schedule on %s - %s",
								fleets.get(fleetId).getCode(), trip.getDepartureTime(),
								trip.getArrivalTime()),
						HttpStatus.CONFLICT);
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
				.orElseThrow(() -> new BusinessException("Fleet & Departure time request is empty",
						HttpStatus.BAD_REQUEST));
		LocalDateTime maxReturnArrival = request.getScheduledFleets().stream()
				.map(f -> f.getDepartureSchedule()
						.plusHours(Integer.sum(tripDuration, turnaroundBuffer)))
				.max(LocalDateTime::compareTo)
				.orElseThrow(() -> new BusinessException("Fleet & Departure time request is empty",
						HttpStatus.BAD_REQUEST));

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

		return createdTrips.stream().map(TripMapper::summary)
				.toList();
	}
}

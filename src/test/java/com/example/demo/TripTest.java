package com.example.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.DTO.request.CreateTripRequest;
import com.example.demo.DTO.response.TripResponse;
import com.example.demo.service.TripService;

@SpringBootTest
public class TripTest {
    @Autowired
    TripService service;

    @Test
    void createTripsSuccess() {
        CreateTripRequest.FleetScheduleRequest scheduledFleet1 = new CreateTripRequest.FleetScheduleRequest();
        scheduledFleet1.setFleetId(1);
        scheduledFleet1.setDepartureSchedule(LocalDateTime.of(
                2026, 07, 10,
                23, 0, 0));
        CreateTripRequest.FleetScheduleRequest scheduledFleet2 = new CreateTripRequest.FleetScheduleRequest();
        scheduledFleet2.setFleetId(12);
        scheduledFleet2.setDepartureSchedule(LocalDateTime.of(
                2026, 07, 11,
                0, 0, 0));
        CreateTripRequest.FleetScheduleRequest scheduledFleet3 = new CreateTripRequest.FleetScheduleRequest();
        scheduledFleet3.setFleetId(20);
        scheduledFleet3.setDepartureSchedule(LocalDateTime.of(
                2026, 07, 10,
                22, 0, 0));
        
        List<CreateTripRequest.FleetScheduleRequest> scheduledFleets = new ArrayList<>();
        scheduledFleets.add(scheduledFleet1);
        scheduledFleets.add(scheduledFleet2);
        scheduledFleets.add(scheduledFleet3);

        CreateTripRequest trip = new CreateTripRequest();
        trip.setDepartureTerminalId(2);
        trip.setDestinationTerminalId(8);
        trip.setEstimatedDuration(10);
        trip.setTurnaroundBufferDuration(3);
        trip.setScheduledFleets(scheduledFleets);

        List<TripResponse.CompleteResponse> responses = service.bulkCreate(trip);

        Assertions.assertNotNull(responses);

        // Need Adjustment
        Integer idx = 0;
        for (CreateTripRequest.FleetScheduleRequest expected : scheduledFleets) {
            Assertions.assertEquals("Bandung", responses.get(idx).getDepartureTerminalCity());
            Assertions.assertEquals("Denpasar", responses.get(idx).getDestinationTerminalCity());
            Assertions.assertEquals(expected.getDepartureSchedule(), responses.get(idx).getDepartureTime());
            Assertions.assertEquals(expected.getDepartureSchedule().plusHours(0), responses.get(idx).getArrivalTime());
            Assertions.assertEquals("PT. Sinar Jaya", responses.get(idx).getBusName());
            // Assertions.assertEquals(Types.ECONOMY, responses.get(idx).getBusType());

            idx++;
        }

    }

    @Test
    void getAvailableTripSuccess() {
        List<TripResponse.CompleteResponse> response = service.GetAvailableTrip(
                "Jakarta",
                "Surabaya",
                LocalDate.of(2026, 07, 10));

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Jakarta", response.get(0).getDepartureTerminalCity());
        Assertions.assertEquals("Terminal Kampung Rambutan", response.get(0).getDepartureTerminal());
        Assertions.assertEquals(LocalDate.of(2026, 07, 10), response.get(0).getDepartureTime().toLocalDate());
    }
}

package com.example.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.DTO.request.CreateTripRequest;
import com.example.demo.DTO.response.TripResponse;
import com.example.demo.enums.Types;
import com.example.demo.service.TripService;

@SpringBootTest
public class TripTest {
    @Autowired
    TripService service;

    @Test
    void createTripsSuccess() {
        CreateTripRequest.FleetScheduleRequest scheduledFleet1 = new CreateTripRequest.FleetScheduleRequest();
        scheduledFleet1.setFleetId(1);
        scheduledFleet1.setDeparture(LocalDateTime.of(
                2026, 07, 10,
                23, 0, 0));
        scheduledFleet1.setDeparture(LocalDateTime.of(
                2026, 07, 11,
                8, 0, 0));
        CreateTripRequest.FleetScheduleRequest scheduledFleet2 = new CreateTripRequest.FleetScheduleRequest();
        scheduledFleet2.setFleetId(12);
        scheduledFleet2.setDeparture(LocalDateTime.of(
                2026, 07, 11,
                0, 0, 0));
        scheduledFleet2.setDeparture(LocalDateTime.of(
                2026, 07, 11,
                9, 0, 0));
        CreateTripRequest.FleetScheduleRequest scheduledFleet3 = new CreateTripRequest.FleetScheduleRequest();
        scheduledFleet3.setFleetId(20);
        scheduledFleet3.setDeparture(LocalDateTime.of(
                2026, 07, 10,
                22, 0, 0));
        scheduledFleet3.setDeparture(LocalDateTime.of(
                2026, 07, 11,
                7, 0, 0));

        List<CreateTripRequest.FleetScheduleRequest> scheduledFleets = new ArrayList<>();
        scheduledFleets.add(scheduledFleet1);
        scheduledFleets.add(scheduledFleet2);
        scheduledFleets.add(scheduledFleet3);

        CreateTripRequest trip = new CreateTripRequest();
        trip.setDepartureTerminalId(2);
        trip.setDestinationTerminalId(8);
        trip.setScheduledFleets(scheduledFleets);

        List<TripResponse.CompleteResponse> responses = service.bulkCreate(trip);

        Assertions.assertNotNull(responses);

        Integer idx = 0;
        for (CreateTripRequest.FleetScheduleRequest expected : scheduledFleets) {
            Assertions.assertEquals("Bandung", responses.get(idx).getDepartureTerminalCity());
            Assertions.assertEquals("Bali", responses.get(idx).getDestinationTerminalCity());
            Assertions.assertEquals(expected.getDeparture(), responses.get(idx).getDepartureTime());
            Assertions.assertEquals(expected.getArrival(), responses.get(idx).getArrivalTime());
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

package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.DTO.request.CreateBusRequestDto;
import com.example.demo.DTO.request.CreateFleetRequestDto;
import com.example.demo.DTO.response.BusResponseDto;
import com.example.demo.service.BusService;

@SpringBootTest
public class BusTest {
    @Autowired
    private BusService service;

    @Test
    void testAddBusSuccess() {
        CreateFleetRequestDto fleetRequest1 = new CreateFleetRequestDto();
        fleetRequest1.setTypeId(1);
        fleetRequest1.setQuantity(10);
        CreateFleetRequestDto fleetRequest2 = new CreateFleetRequestDto();
        fleetRequest2.setTypeId(2);
        fleetRequest2.setQuantity(8);
        CreateFleetRequestDto fleetRequest3 = new CreateFleetRequestDto();
        fleetRequest3.setTypeId(3);
        fleetRequest3.setQuantity(5);
        CreateFleetRequestDto fleetRequest4 = new CreateFleetRequestDto();
        fleetRequest4.setTypeId(1);
        fleetRequest4.setQuantity(20);
        CreateFleetRequestDto fleetRequest5 = new CreateFleetRequestDto();
        fleetRequest5.setTypeId(2);
        fleetRequest5.setQuantity(10);
        CreateFleetRequestDto fleetRequest6 = new CreateFleetRequestDto();
        fleetRequest6.setTypeId(3);
        fleetRequest6.setQuantity(10);

        List<CreateFleetRequestDto> fleets = new ArrayList<>();
        fleets.add(fleetRequest1);
        fleets.add(fleetRequest2);
        fleets.add(fleetRequest3);
        List<CreateFleetRequestDto> fleets2 = new ArrayList<>();
        fleets2.add(fleetRequest4);
        fleets2.add(fleetRequest5);
        fleets2.add(fleetRequest6);

        CreateBusRequestDto busRequest1 = new CreateBusRequestDto();
        busRequest1.setFleets(fleets);
        busRequest1.setName("PT. Sinar Jaya");
        CreateBusRequestDto busRequest2 = new CreateBusRequestDto();
        busRequest2.setFleets(fleets2);
        busRequest2.setName("PT. Jaya");

        List<CreateBusRequestDto> buses = new ArrayList<>();
        buses.add(busRequest1);
        buses.add(busRequest2);

        List<BusResponseDto> response = service.bulkCreateBus(buses);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(2, response.size());
        Assertions.assertEquals(23, response.get(0).fleets().size());
        Assertions.assertEquals(40, response.get(1).fleets().size());
        Assertions.assertEquals("SJ-0001", response.get(0).fleets().get(0).code());
        Assertions.assertEquals("JAY-0001", response.get(1).fleets().get(0).code());
    }
}

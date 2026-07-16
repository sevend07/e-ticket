package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.DTO.request.CreateTypeRequestDto;
import com.example.demo.enums.Types;
import com.example.demo.model.Type;
import com.example.demo.service.TypeService;

@SpringBootTest
public class TypeTest {
    @Autowired
    TypeService service;

    @Test
    void testCreateTypeSucces() {
        CreateTypeRequestDto request1 = new CreateTypeRequestDto();
        request1.setType(Types.ECONOMY);
        request1.setTotalSeat(30);
        request1.setPrice(100000);
        
        CreateTypeRequestDto request2 = new CreateTypeRequestDto();
        request2.setType(Types.EXECUTIVE);
        request2.setTotalSeat(20);
        request2.setPrice(200000);
        
        CreateTypeRequestDto request3 = new CreateTypeRequestDto();
        request3.setType(Types.SLEEPER);
        request3.setTotalSeat(10);
        request3.setPrice(300000);

        List<CreateTypeRequestDto> typeList = new ArrayList<>();
        typeList.add(request1);
        typeList.add(request2);
        typeList.add(request3);

        List<Type> response = service.create(typeList);

        Type type = response.get(0);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(3, response.size());
        Assertions.assertEquals(Types.ECONOMY, type.getType());

    }
}

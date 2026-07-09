package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.DTO.request.RegisterRequestDto;
import com.example.demo.service.AuthService;

@SpringBootTest
public class AuthTests {
    // @Autowired
    // private UserRepository userRepo;

    @Autowired
    private AuthService service;

    @Test
    void testRegisterSuccess() {
        RegisterRequestDto request = new RegisterRequestDto(
                "benidwiry", "ben111", "beni", "beni@gmail.com",
                "rembang", "081111111111");

        String response = service.register(request);

        Assertions.assertEquals("Register Successfull", response);
    }
}

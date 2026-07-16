package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.request.LoginRequestDto;
import com.example.demo.DTO.response.LoginResponse;
import com.example.demo.service.AuthService;
import com.example.demo.util.GenerateResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDto request) {
        LoginResponse data;

        try {
            data = service.login(request);
        } catch (Exception e) {
            return GenerateResponse.generateResponseEntity(HttpStatus.OK, e.getMessage());
        }
        
        return GenerateResponse.generateResponseEntity(HttpStatus.OK, "Login Success", data);
    }
}

package com.example.demo.DTO.request;

public record RegisterRequestDto(
    String username,
    String password,
    String name,
    String email,
    String address,
    String phoneNumber
) {}

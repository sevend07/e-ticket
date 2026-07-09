package com.example.demo.DTO.response;

public record LoginResponse(
    Integer userId,
    String username,
    Integer personId,
    String name
) {
} 

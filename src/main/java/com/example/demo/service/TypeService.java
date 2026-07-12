package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.request.CreateTypeRequestDto;
import com.example.demo.model.Type;
import com.example.demo.repository.TypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TypeService {
    private final TypeRepository repo;

    public List<Type> create(List<CreateTypeRequestDto> requests) {
        List<Type> newType = new ArrayList<>();
        for (CreateTypeRequestDto request : requests) {
            Type type = new Type();
            type.setType(request.getType());
            type.setTotalSeat(request.getTotalSeat());
            type.setPrice(request.getPrice());

            newType.add(type);
        }

        try {
            List<Type> createdType = repo.saveAll(newType);

            return createdType;
        } catch (Exception e) {
            throw new RuntimeException(String.format("Error: %s", e.getMessage()));
        }

    }
}

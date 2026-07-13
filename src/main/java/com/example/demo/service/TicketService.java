package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.response.TicketResponse;
import com.example.demo.mapper.TicketMapper;
import com.example.demo.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository repo;

    public List<TicketResponse> getActiveTicketByUser(Integer userId) {
        return repo.findActiveTicketByUser(userId).stream()
                .map(TicketMapper::toResponse).toList();
    }
}

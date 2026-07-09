package com.example.demo.service;

import com.example.demo.DTO.request.CreateTerminalRequestDto;
import com.example.demo.DTO.response.TerminalResponseDto;
import com.example.demo.mapper.TerminalMapper;
import com.example.demo.model.Terminal;
import com.example.demo.repository.TerminalRepository;

public class TerminalService {
    private final TerminalRepository terminalRepo;
    private TerminalMapper terminalMapper;

    public TerminalService(TerminalRepository terminalRepo) {
        this.terminalRepo = terminalRepo;
    }

    public TerminalResponseDto create(CreateTerminalRequestDto request) {
        Terminal terminal = new Terminal();
        terminal.setName(request.getName());
        terminal.setCity(request.getCity());

        try {
            terminalRepo.save(terminal);
            return terminalMapper.toDto(terminal);
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("Error :%s", e.getMessage()));
        }
    }
}

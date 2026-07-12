package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.request.CreateTerminalRequestDto;
import com.example.demo.DTO.response.TerminalResponseDto;
import com.example.demo.mapper.TerminalMapper;
import com.example.demo.model.Terminal;
import com.example.demo.repository.TerminalRepository;

@Service
public class TerminalService {
    private final TerminalRepository terminalRepo;
    private TerminalMapper terminalMapper;

    public TerminalService(TerminalRepository terminalRepo) {
        this.terminalRepo = terminalRepo;
    }

    public List<Terminal> search(String keyword) {
        return terminalRepo.search(keyword);
    }

    public Terminal findById(Integer id) {
        return terminalRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Terminal not found"));
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

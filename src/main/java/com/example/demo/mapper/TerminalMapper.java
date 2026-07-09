package com.example.demo.mapper;

import java.util.List;

import com.example.demo.DTO.response.TerminalResponseDto;
import com.example.demo.model.Terminal;

public class TerminalMapper {
    public TerminalResponseDto toDto(Terminal terminal) {
        if (terminal == null)
            return null;

        return new TerminalResponseDto(
                terminal.getId(),
                terminal.getName(),
                terminal.getCity());
    }

    public List<TerminalResponseDto> toDto(List<Terminal> terminal) {
        if (terminal == null)
            return null;

        return terminal.stream().map(this::toDto).toList();
    }
}

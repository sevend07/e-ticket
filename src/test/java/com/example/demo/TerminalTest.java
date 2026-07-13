package com.example.demo;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.model.Terminal;
import com.example.demo.service.TerminalService;

@SpringBootTest
public class TerminalTest {
    @Autowired
    TerminalService service;

    @Test
    void searchTerminal_Found() {
        String name = "kampung rambutan";
        String city = "bandung";
        List<Terminal> terminalsByCity = service.search(city);
        List<Terminal> terminalsByName = service.search(name);

        for (Terminal terminal : terminalsByCity) {
            Assertions.assertEquals("Bandung", terminal.getCity());
        }

        for (Terminal terminal : terminalsByName) {
            Assertions.assertEquals("Terminal Kampung Rambutan", terminal.getName());
        }
    }
}

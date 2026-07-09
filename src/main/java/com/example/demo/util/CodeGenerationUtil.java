package com.example.demo.util;

import com.example.demo.model.Bus;
import com.example.demo.repository.FleetRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CodeGenerationUtil {
    private final FleetRepository fleetRepository;

    public String generateFleetCode(Bus bus) {

        String prefix = generatePrefix(bus.getName());

        long counter = fleetRepository.countByCodeStartingWith(prefix) + 1;

        return "%s-%04d".formatted(prefix, counter);
    }

    private String generatePrefix(String busName) {

        // Hilangkan PT atau PT.
        String cleaned = busName
                .replaceFirst("(?i)^PT\\.?\\s*", "")
                // Hilangkan angka dan karakter selain huruf/spasi
                .replaceAll("[^a-zA-Z\\s]", "")
                .trim()
                .replaceAll("\\s+", " ");

        String[] words = cleaned.split(" ");

        if (words.length >= 2) {
            return ("" + words[0].charAt(0) + words[1].charAt(0))
                    .toUpperCase();
        }

        return cleaned.substring(0, Math.min(3, cleaned.length()))
                .toUpperCase();
    }
}

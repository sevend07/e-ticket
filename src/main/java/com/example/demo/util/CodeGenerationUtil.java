package com.example.demo.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.example.demo.model.Bus;
import com.example.demo.repository.FleetRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CodeGenerationUtil {
    private final FleetRepository fleetRepository;

    private final Map<String, AtomicLong> prefixCounters = new ConcurrentHashMap<>();

    public String generateFleetCode(Bus bus) {
        String prefix = generatePrefix(bus.getName());

        long counter = prefixCounters
                .computeIfAbsent(prefix, p -> new AtomicLong(fleetRepository.countByCodeStartingWith(p)))
                .incrementAndGet();

        return "%s-%04d".formatted(prefix, counter);
    }

    private String generatePrefix(String busName) {
        String cleaned = busName
                .replaceFirst("(?i)^PT\\.?\\s*", "")
                .replaceAll("[^a-zA-Z\\s]", "")
                .trim()
                .replaceAll("\\s+", " ");

        String[] words = cleaned.split(" ");

        if (words.length >= 2) {
            return ("" + words[0].charAt(0) + words[1].charAt(0)).toUpperCase();
        }

        return cleaned.substring(0, Math.min(3, cleaned.length())).toUpperCase();
    }
}

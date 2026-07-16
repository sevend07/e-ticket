package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Role;
import com.example.demo.service.RoleService;

import lombok.RequiredArgsConstructor;

@RestController("api/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService service;

    @GetMapping("{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Integer Id) {
        Role response = service.findById(Id).get();
        return ResponseEntity.ok(response);
    }
}

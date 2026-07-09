package com.example.demo.service;

import javax.transaction.Transactional;

import org.springframework.security.authentication.BadCredentialsException;

import com.example.demo.DTO.request.RegisterRequestDto;
import com.example.demo.DTO.response.LoginResponse;
import com.example.demo.model.Person;
import com.example.demo.model.User;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.UserRepository;

public class AuthService {
    private final UserRepository userRepo;
    private final PersonRepository personRepo;

    public AuthService(PersonRepository personRepo, UserRepository userRepo) {
        this.userRepo = userRepo;
        this.personRepo = personRepo;
    }

    public LoginResponse login(String username, String password) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Username atau password salah"));

        if (password != user.getPassword())
            throw new BadCredentialsException("Username atau password salah");

        return new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getPerson().getId(),
                user.getPerson().getName());
    }

    @Transactional
    public String register(RegisterRequestDto request) {

        if (userRepo.existsByUsername(request.username()))
            throw new RuntimeException("Username already exists");

        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setPassword(request.password());

        Person newPerson = new Person();
        newPerson.setName(request.name());
        newPerson.setEmail(request.email());
        newPerson.setAddress(request.address());
        newPerson.setPhoneNumber(request.phoneNumber());

        try {
            userRepo.save(newUser);
            personRepo.save(newPerson);
            return "Register Successfull";
        } catch (Exception e) {
            throw new RuntimeException(String.format(
                "Error :%s", e.getMessage())
            );
        }

    }

}

package com.example.demo.service;

import javax.transaction.Transactional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.request.RegisterRequestDto;
import com.example.demo.DTO.response.LoginResponse;
import com.example.demo.model.Person;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepo;

    // public AuthService(PersonRepository personRepo, UserRepository userRepo,
    // RoleRepository roleRepository) {
    // this.userRepo = userRepo;
    // this.personRepo = personRepo;
    // this.roleRepository = roleRepository;
    // }

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

        Role role = roleRepository.findFirstByOrderByLevelDesc()
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Person newPerson = new Person();
        newPerson.setName(request.name());
        newPerson.setEmail(request.email());
        newPerson.setAddress(request.address());
        newPerson.setPhoneNumber(request.phoneNumber());

        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setPassword(request.password());
        newUser.setRole(role);

        newPerson.setUser(newUser);
        newUser.setPerson(newPerson);

        try {
            userRepo.save(newUser);
            return "Register Successfull";
        } catch (Exception e) {
            throw new RuntimeException(String.format(
                    "Error :%s", e.getMessage()));
        }

    }

}

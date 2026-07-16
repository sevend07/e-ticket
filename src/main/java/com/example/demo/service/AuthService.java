package com.example.demo.service;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.request.LoginRequestDto;
import com.example.demo.DTO.request.RegisterRequestDto;
import com.example.demo.DTO.response.LoginResponse;
import com.example.demo.exception.BusinessException;
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
    // private final

    // public AuthService(PersonRepository personRepo, UserRepository userRepo,
    // RoleRepository roleRepository) {
    // this.userRepo = userRepo;
    // this.personRepo = personRepo;
    // this.roleRepository = roleRepository;
    // }
    
    @Transactional
    public LoginResponse login(LoginRequestDto request) {
        System.out.println(request.username());
        System.out.println(request.password());
        User user = userRepo.findByUsername(request.username())
                .orElseThrow(() -> new BadCredentialsException("Username atau password salah 1"));

        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        // if (BCrypt.checkpw(request.username(), user.getPassword()))
        // throw new BadCredentialsException("Username atau password salah");
        if (!request.password().equals(user.getPassword()))
            throw new BadCredentialsException("Username atau password salah 2");

        return new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getPerson().getId(),
                user.getPerson().getName());
    }

    @Transactional
    public String register(RegisterRequestDto request) {

        if (userRepo.existsByUsername(request.username()))
            throw new BusinessException("Username already exists",
                    HttpStatus.CONFLICT);

        Role role = roleRepository.findFirstByOrderByLevelDesc()
                .orElseThrow(() -> new BusinessException("Role not found",
                        HttpStatus.NOT_FOUND));

        Person newPerson = new Person();
        newPerson.setName(request.name());
        newPerson.setEmail(request.email());
        newPerson.setAddress(request.address());
        newPerson.setPhoneNumber(request.phoneNumber());

        User newUser = new User();
        newUser.setUsername(request.username());
        newUser.setPassword(BCrypt.hashpw(request.password(), BCrypt.gensalt()));
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

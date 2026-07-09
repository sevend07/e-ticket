package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("""
            SELECT u 
            FROM User u
            LEFT JOIN FETCH u.person
            WHERE u.username = :username
    """)
    Optional<User>findByUsername(String username);

    Boolean existsByUsername(String username);
}

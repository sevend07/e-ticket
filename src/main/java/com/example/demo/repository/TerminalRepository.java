package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Terminal;

@Repository
public interface TerminalRepository extends JpaRepository<Terminal, Integer> {
    @Query("""
            SELECT t FROM Terminal t
            WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(t.city) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<Terminal> search(@Param("keyword") String keyword);

}

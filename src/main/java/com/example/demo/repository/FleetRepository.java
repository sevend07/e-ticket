package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Fleet;

@Repository
public interface FleetRepository extends JpaRepository<Fleet, Integer> {

    Integer countByCodeStartingWith(String prefix);

    @Query("""
            SELECT f FROM Fleet f
            JOIN f.bus b
            WHERE b.id = :busId
            AND f.id NOT IN(
                SELECT t.fleet.id FROM Trip t
                WHERE t.departureTime <= :arrival
                AND t.arrivalTime >= :departure
            )
            """)
    List<Fleet> findAvailableFleetByBusAndSchedule(
        @Param("busId") Integer busId,
        @Param("arrival") LocalDateTime arrival,
        @Param("departure") LocalDateTime departure
    );

}

package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.DTO.response.TripResponse;
import com.example.demo.model.Trip;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {
    @Query("""
            SELECT new com.example.demo.DTO.response.TripResponse$CompleteResponse(
                tr.id, tr.departureTime, tr.arrivalTime,
                dep_t.name, des_t.name, dep_t.city, des_t.city, b.name,
                ty.type, ty.price
            ) FROM Trip tr
                JOIN tr.departureTerminal dep_t
                JOIN tr.destinationTerminal des_t
                JOIN tr.fleet f
                JOIN f.bus b
                JOIN f.type ty
            WHERE dep_t.city = :departureTerminal
            AND des_t.city = :destinationTerminal
            AND tr.departureTime BETWEEN :start AND :end
            """)
    List<TripResponse.CompleteResponse> findTrips(
            @Param("departureTerminal") String departureTerminal,
            @Param("destinationTerminal") String destinationTerminal,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    // @Query("""
    //         SELECT COUNT(*)
    //         FROM Trip
    //         WHERE fleet_id = :fleetId
    //         AND departure_time <= :arrivalTime
    //         AND arrival_time >= :departureTime
    //         """)
    // boolean existsOverlap(
    //         @Param("fleetId") Integer fleetId,
    //         @Param("departureTime") LocalDateTime departureTime,
    //         @Param("arrivalTime") LocalDateTime arrivalTime);

    // boolean existsByFleetIdAndDepartureTime(Integer fleetId, LocalDateTime departureTime);

    // @Query("""
    //         SELECT arrival_time
    //         FROM Trip
    //         WHERE fleet_id = :fleetId
    //         ORDER BY arrival_time DESC
    //         LIMIT 1
    //         """)
    // LocalDateTime findLatestArrivalByFleet(
    //         @Param("fleetId") Integer fleetId);

    @Query("""
            SELECT DISTINCT t.fleet.id FROM Trip t
            WHERE t.fleet.id IN :fleetIds
            AND departureTime <= :maxArrival
            AND arrivalTime >= :minDeparture
            """)
    List<Trip> findConflictingTrips(
            @Param("fleetIds") Set<Integer> fleetIds,
            @Param("minDeparture") LocalDateTime minDeparture,
            @Param("maxArrival") LocalDateTime maxArrival);
}

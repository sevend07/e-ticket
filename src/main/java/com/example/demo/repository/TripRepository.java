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
                    tr.id,
                    tr.departureTime,
                    tr.arrivalTime,
                    dep.name,
                    des.name,
                    dep.city,
                    des.city,
                    b.name,
                    f.code,
                    ty.type,
                    ty.price
            )
            FROM Trip tr
            JOIN tr.departureTerminal dep
            JOIN tr.destinationTerminal des
            JOIN tr.fleet f
            JOIN f.bus b
            JOIN f.type ty
            LEFT JOIN Booking bk
                    ON bk.trip = tr
                    AND bk.status = com.example.demo.enums.BookingStatus.BOOKED
            LEFT JOIN BookingItem bi
                    ON bi.booking = bk
            WHERE dep.id = :departureTerminalId
            AND des.id = :destinationTerminalId
            AND tr.departureTime BETWEEN :start AND :end
            GROUP BY
                    tr.id,
                    tr.departureTime,
                    tr.arrivalTime,
                    dep.name,
                    des.name,
                    dep.city,
                    des.city,
                    b.name,
                    ty.type,
                    ty.price,
                    ty.totalSeat
            HAVING (ty.totalSeat - COUNT(bi.id)) >= :passengerCount
            """)
    List<TripResponse.CompleteResponse> findAvailableTrips(
            @Param("departureTerminalId") Integer departureTerminalId,
            @Param("destinationTerminalId") Integer destinationTerminalId,
            @Param("passengerCount") Integer passengerCount,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    // @Query("""
    // SELECT COUNT(*)
    // FROM Trip
    // WHERE fleet_id = :fleetId
    // AND departure_time <= :arrivalTime
    // AND arrival_time >= :departureTime
    // """)
    // boolean existsOverlap(
    // @Param("fleetId") Integer fleetId,
    // @Param("departureTime") LocalDateTime departureTime,
    // @Param("arrivalTime") LocalDateTime arrivalTime);

    // boolean existsByFleetIdAndDepartureTime(Integer fleetId, LocalDateTime
    // departureTime);

    @Query("""
            SELECT t FROM Trip t
            WHERE t.fleet.id IN :fleetIds
            AND departureTime <= :maxReturnArrival
            AND arrivalTime >= :minOutboundDeparture
            """)
    List<Trip> findTripByTimeWindow(
            @Param("fleetIds") Set<Integer> fleetIds,
            @Param("minOutboundDeparture") LocalDateTime minOutboundDeparture,
            @Param("maxReturnArrival") LocalDateTime maxReturnArrival);
}

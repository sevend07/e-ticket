package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.DTO.response.TripResponse.SeatInformation;
import com.example.demo.model.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {
    @Query("""
            SELECT s FROM Seat s
            WHERE s.type.id = :typeId
            AND NOT EXISTS (
                SELECT 1
                FROM BookingItem bi
                WHERE bi.seat.id = s
                    AND bi.booking.trip.id = :tripId
                    AND bi.booking.status = 'BOOKED'
            )
            """)
    List<Seat> findAvailableSeatByTrip(
            @Param("tripId") Integer tripId,
            @Param("typeId") Integer typeId);

    @Query("""
            SELECT new com.example.demo.DTO.response.TripResponse$SeatInformation(
                s.id,
                s.code,
                CASE
                    WHEN EXISTS(
                        SELECT 1 FROM BookingItem bi
                        WHERE bi.seat = s
                            AND bi.booking.trip.id = :tripId
                            AND bi.booking.status = 'BOOKED'
                    ) THEN true ELSE false
                END
            ) FROM Seat s
                WHERE s.type.id = :typeId
            ORDER BY s.code
            """)
    List<SeatInformation> findSeatInformationByTrip(
            @Param("tripId") Integer tripId,
            @Param("typeId") Integer typeId);
}

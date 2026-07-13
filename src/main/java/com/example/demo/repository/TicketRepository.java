package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    @Query("""
            SELECT t FROM Ticket t
            WHERE t.bookingItem.booking.user.id = :userId
                AND t.status = 'ACTIVE'
            """)
    List<Ticket> findActiveTicketByUser(
            @Param("userId") Integer userId);
}

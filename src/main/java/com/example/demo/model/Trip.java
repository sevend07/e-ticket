package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    @OneToMany(mappedBy = "trip")
    private List<Booking> bookings;

    @ManyToOne
    @JoinColumn(name = "fleet_id")
    private Fleet fleet;

    @ManyToOne
    @JoinColumn(name = "departure_terminal_id")
    private Terminal departureTerminal;

    @ManyToOne
    @JoinColumn(name = "destination_terminal_id")
    private Terminal destinationTerminal;
}

package com.example.demo.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Terminal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    // @OneToMany(mappedBy = "currentTerminal")
    // private List<Fleet> fleets;

    @OneToMany(mappedBy = "departureTerminal")
    private List<Trip> departureTrips;

    @OneToMany(mappedBy = "destinationTerminal")
    private List<Trip> destinationTrips;
}

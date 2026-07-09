package com.example.demo.model;

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
public class Fleet {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "code")
    private String code;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;

    @OneToMany(mappedBy = "fleet")
    private List<Trip> trips;
}

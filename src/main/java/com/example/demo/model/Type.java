package com.example.demo.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.example.demo.enums.Types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Types type;

    @Column(name = "total_seat")
    private Integer totalSeat;

    @Column(name = "price")
    private Integer price;

    @OneToMany(mappedBy = "type")
    private List<Fleet> fleets;

    @OneToMany(mappedBy = "type")
    private List<Seat> seats;

}

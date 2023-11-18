package com.example.bike_sharing_location.domain;

import jakarta.persistence.*;

@Entity
@Table(name="Bikes")
public class Bike {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    String tmp;
}

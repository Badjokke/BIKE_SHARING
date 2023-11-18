package com.example.bike_sharing_location.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "Stands")
public class Stand {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    String tmp;
}

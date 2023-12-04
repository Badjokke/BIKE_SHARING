package com.example.bike_sharing_location.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Stands")
public class Stand {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    Double longitude;
    @NotNull
    Double latitude;

    public Stand() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Stand(Long id, String name, double longitude, double latitude) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}

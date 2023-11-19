package com.example.bike_sharing_location.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name="Bikes")
public class Bike {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Double longitude;
    @NotNull
    private Double latitude;
    @DateTimeFormat
    @NotNull
    private Date lastService;
    @ManyToOne
    @JoinColumn(name="standId",referencedColumnName = "id")
    @Nullable
    private Stand stand;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getLastService() {
        return lastService;
    }

    public void setLastService(Date lastService) {
        this.lastService = lastService;
    }

    @Nullable
    public Stand getStand() {
        return stand;
    }

    public void setStand(@Nullable Stand stand) {
        this.stand = stand;
    }

    public Bike(Long id, Double longitude, Double latitude, Date lastService, @Nullable Stand stand) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.lastService = lastService;
        this.stand = stand;
    }

    public Bike() {
    }
}

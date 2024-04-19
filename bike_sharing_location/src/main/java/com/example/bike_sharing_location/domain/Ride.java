package com.example.bike_sharing_location.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name="Rides")
public class Ride {
    public Ride(Long userId, Bike bike, Stand startStand, Stand endStand, Date startTimestamp, Date endTimestamp) {
        this.userId = userId;
        this.bike = bike;
        this.startStand = startStand;
        this.endStand = endStand;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long userId;
    @ManyToOne
    @NotNull
    @JoinColumn(name="bikeId",referencedColumnName = "id")
    private Bike bike;

    @ManyToOne
    @NotNull
    @JoinColumn(name="startStandId",referencedColumnName = "id")
    private Stand startStand;

    @ManyToOne
    @NotNull
    @JoinColumn(name="endStandId",referencedColumnName = "id")
    private Stand endStand;

    @DateTimeFormat
    @NotNull
    private Date startTimestamp;

    @DateTimeFormat
    @NotNull
    private Date endTimestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Nullable
    public Bike getBike() {
        return bike;
    }

    public void setBike( Bike bike) {
        this.bike = bike;
    }

    public Stand getStartStand() {
        return startStand;
    }

    public void setStartStand(Stand startStand) {
        this.startStand = startStand;
    }

    public Stand getEndStand() {
        return endStand;
    }

    public void setEndStand( Stand endStand) {
        this.endStand = endStand;
    }

    public Date getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Date startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Date getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Date endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public Ride(Long id, Long userId, Bike bike, Stand startStand, Stand endStand, Date startTimestamp, Date endTimestamp) {
        this.id = id;
        this.userId = userId;
        this.bike = bike;
        this.startStand = startStand;
        this.endStand = endStand;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    public Ride() {
    }
}

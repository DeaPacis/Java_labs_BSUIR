package com.labEpam.timeCounter.jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "values")
public class ValuesDB {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "speed")
    private Float speed;
    @Column(name = "distance")
    private Float distance;
    @Column(name = "time")
    private Float time;

    public ValuesDB(Float speed, Float distance) {
        this.speed = speed;
        this.distance = distance;
    }

    public ValuesDB(Float speed, Float distance, Float time) {
        this.speed = speed;
        this.distance = distance;
        this.time = time;
    }

    public ValuesDB() {}

    public Integer getId() {
        return id;
    }

    public Float getSpeed() {
        return speed;
    }

    public Float getDistance() {
        return distance;
    }

    public Float getTime() {
        return time;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public void setTime(Float time) {
        this.time = time;
    }
}

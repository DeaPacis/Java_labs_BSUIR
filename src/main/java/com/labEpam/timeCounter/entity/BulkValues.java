package com.labEpam.timeCounter.entity;

public class BulkValues {
    private Float speed;
    private Float distance;

    public BulkValues() {
        this.speed = 0.0f;
        this.distance = 0.0f;
    }

    public BulkValues(Float speed, Float distance) {
        this.speed = speed;
        this.distance = distance;
    }

    public void setNull() {
        this.speed = 0.0f;
        this.distance = 0.0f;
    }

    public Float getDistance() {
        return distance;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }
}

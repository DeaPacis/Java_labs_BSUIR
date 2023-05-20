package com.labEpam.timeCounter.entity;

public class Values {
    protected Float speed;
    protected Float distance;
    protected Float time;
    public Values() {}
    public Values(Float speed, Float distance, Float time) {
        this.speed  = speed;
        this.distance = distance;
        this.time = time;
    }
    public Values(Float speed, Float distance) {
        this.speed  = speed;
        this.distance = distance;
    }
    public float getSpeed() {
        return speed;
    }

    public float getDistance() {
        return distance;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }
}

package com.labEpam.timeCounter.entity;

import java.util.List;

public class ExtendedPost {

    private List<Values> result;
    private Float maxTime;
    private Float minTime;
    private Float averageTime;

    public ExtendedPost(List<Values> result, Float maxTime, Float minTime, Float averageTime) {
        this.result = result;
        this.maxTime = maxTime;
        this.minTime = minTime;
        this.averageTime = averageTime;
    }

    public ExtendedPost() {}

    public List<Values> getResult() {
        return result;
    }

    public Float getMaxTime() {
        return maxTime;
    }

    public Float getMinTime() {
        return minTime;
    }

    public Float getAverageTime() {
        return averageTime;
    }

    public void setResult(List<Values> result) {
        this.result = result;
    }

    public void setMaxTime(Float maxTime) {
        this.maxTime = maxTime;
    }

    public void setMinTime(Float minTime) {
        this.minTime = minTime;
    }

    public void setAverageTime(Float averageTime) {
        this.averageTime = averageTime;
    }
}

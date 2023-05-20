package com.labEpam.timeCounter.services;

import org.springframework.stereotype.Service;

@Service
public class CounterService {

    private Integer synchronisedCounter;
    private Integer unsynchronisedCounter;

    public CounterService() {

        this.synchronisedCounter = 0;
        this.unsynchronisedCounter = 0;
    }

    public CounterService(Integer synchronisedCounter, Integer unsynchronisedCounter) {

        this.synchronisedCounter = synchronisedCounter;
        this.unsynchronisedCounter = unsynchronisedCounter;
    }

    public synchronized void incrementSynchronisedCounter() {

        synchronisedCounter++;
    }

    public void incrementUnsynchronisedCounter() {

        unsynchronisedCounter++;
    }

    public Integer getSynchronisedCounter() {

        return synchronisedCounter;
    }

    public Integer getUnsynchronisedCounter() {

        return unsynchronisedCounter;
    }
}

package com.labEpam.timeCounter.entity;

import com.labEpam.timeCounter.services.CounterService;

public class Counter {

    private Integer synchCounter;
    private Integer unsyncCounter;

    public Counter() {

        this.synchCounter = 0;
        this.unsyncCounter = 0;
    }

    public Counter(CounterService counterService) {

        this.synchCounter = counterService.getSynchronisedCounter();
        this.unsyncCounter = counterService.getUnsynchronisedCounter();
    }

    public Integer getSynchCounter() {

        return synchCounter;
    }

    public Integer getUnsyncCounter() {

        return unsyncCounter;
    }
}

package com.labEpam.timeCounter.async;

import com.labEpam.timeCounter.jpa.model.ValuesDB;
import com.labEpam.timeCounter.services.CalculateTime;
import com.labEpam.timeCounter.services.DataBaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class TimeAsync {
    private final DataBaseService dataBaseService;
    private final CalculateTime calculateTime;
    private static final Logger logger = LogManager.getLogger(TimeAsync.class);

    public TimeAsync(DataBaseService dataBaseService, CalculateTime calculateTime) {
        this.dataBaseService = dataBaseService;
        this.calculateTime = calculateTime;
    }

    public Integer createAsync(ValuesDB valuesDB) {

        logger.info("Creating async call");
        ValuesDB valuesDB1 = new ValuesDB();
        valuesDB1.setSpeed(valuesDB.getSpeed());
        valuesDB1.setDistance(valuesDB.getDistance());
        dataBaseService.saveValueDB(valuesDB1);

        return valuesDB1.getId();
    }

    public CompletableFuture<Integer> computeAsync(int id) {

        logger.info("Computing async call");
        return CompletableFuture.supplyAsync(() -> {
            try {
                ValuesDB result = dataBaseService.findOne(id);

                Thread.sleep(15000);
                result.setTime(calculateTime.count(result.getSpeed(), result.getDistance()));
                dataBaseService.saveValueDB(result);

                return result.getId();
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        });

    }

}

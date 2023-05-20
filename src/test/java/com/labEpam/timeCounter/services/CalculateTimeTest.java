package com.labEpam.timeCounter.services;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.labEpam.timeCounter.entity.BulkValues;
import com.labEpam.timeCounter.entity.Values;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CalculateTimeTest {
    private CalculateTime  calculateTime;

    @Test
    public void calculationTimeTest () {
        calculateTime=new CalculateTime();
        Values time = new Values(60.0f, 360.0f);
        Values timeFirst = new Values(20.0f, 600.0f, 30.0f);
        Values timeSecond = new Values(45.0f, 202.5f, 4.5f);
        Values timeThird = new Values(50.5f, 681.75f, 13.5f);
        BulkValues timeBulk = new BulkValues(time.getSpeed(), time.getDistance());
        BulkValues timeBulkFirst = new BulkValues(timeFirst.getSpeed(), timeFirst.getDistance());
        BulkValues timeBulkSecond = new BulkValues(timeSecond.getSpeed(), timeSecond.getDistance());
        BulkValues timeBulkThird = new BulkValues(timeThird.getSpeed(), timeThird.getDistance());
        List<BulkValues> listBulkValues = new ArrayList<BulkValues>();
        listBulkValues.add(timeBulkFirst);
        listBulkValues.add(timeBulkSecond);
        listBulkValues.add(timeBulkThird);
        Float expectedAnswer = 6.0f;
        Float expectedMax = 30.0f;
        Float expectedMin = 4.5f;
        Float expectedAverage = 16.0f;
        Values expectedBulkAnswer = new Values(60.0f, 360.0f, 6.0f);
        List<Values> listValues=new ArrayList<>()  ;
        listValues.add(timeFirst);
        listValues.add(timeSecond);
        listValues.add(timeThird);
       // Assertions.assertEquals(listValues,calculateTime.countBulk(listBulkValues));
        Assertions.assertEquals(listValues.getClass(),calculateTime.countBulk(listBulkValues).getClass());
        Assertions.assertEquals(expectedBulkAnswer.getClass(), calculateTime.time(timeBulk).getClass());
        Assertions.assertEquals(expectedMax, calculateTime.maxTime(listValues));
        Assertions.assertEquals(expectedMin, calculateTime.minTime(listValues));
        Assertions.assertEquals(expectedAverage, calculateTime.averageTime(listValues));
    }
}

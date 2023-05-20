package com.labEpam.timeCounter.services;

import com.labEpam.timeCounter.entity.BulkValues;
import com.labEpam.timeCounter.entity.Values;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CalculateTime {
    public CalculateTime() {}
    public Float count(Float speed, Float distance){
        Float result;
        if (speed == 0.0f && distance == 0.0f) {
            result = 0.0f;
            return result;
        }
        result = distance / speed;
        return result;
    }

    public Values time(BulkValues value) {
        return new Values(value.getSpeed(), value.getDistance(), count(value.getSpeed(), value.getDistance()));
    }

    public Float maxTime(List<Values> values) {
        return (float) values.stream().mapToDouble(Values::getTime).max().getAsDouble();
    }

    public Float minTime(List<Values> values) {
        return (float) values.stream().mapToDouble(Values::getTime).min().getAsDouble();
    }

    public Float averageTime(List<Values> values) {
        return (float) values.stream().mapToDouble(Values::getTime).average().getAsDouble();
    }

    public List<Values> countBulk(List<BulkValues> values) {
        List<Values> result = new ArrayList<>();
        values.forEach(bulkValues -> result.add(time(bulkValues)));
        return result;
    }
}

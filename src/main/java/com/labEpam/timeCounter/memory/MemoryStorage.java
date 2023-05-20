package com.labEpam.timeCounter.memory;

import com.labEpam.timeCounter.controllers.CalculateTimeController;
import com.labEpam.timeCounter.entity.Values;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MemoryStorage {
    private static final Logger logger = LogManager.getLogger(CalculateTimeController.class);
    private Map<Float, Values> dataStorage = new HashMap<Float, Values>();

    public void saveValues(Values value) {

        dataStorage.put(value.getTime(), value);
    }

    public void saveValues(List<Values> value) {
        for (Values values : value) {
            dataStorage.put(values.getTime(), values);
        }
    }

    public boolean checkPut(Values value) {
        logger.info("Check memory storage input");
        return dataStorage.containsValue(value);
    }

    public Values getValue(Float key) {

        return dataStorage.get(key);
    }

    public List<Values> getAllValues() {
        logger.info("Get all values");
        List<Values> allValues = new ArrayList<Values>();
        if(dataStorage.isEmpty()) {
            logger.info("Memory storage is empty");
            return allValues;
        }
        dataStorage.forEach((key, value) -> allValues.add(value));
        return allValues;
    }

    public Integer getValuesAmount() {
        return dataStorage.size();
    }
}

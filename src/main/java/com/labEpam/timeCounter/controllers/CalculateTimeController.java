package com.labEpam.timeCounter.controllers;

import com.labEpam.timeCounter.async.TimeAsync;
import com.labEpam.timeCounter.entity.BulkValues;
import com.labEpam.timeCounter.entity.Counter;
import com.labEpam.timeCounter.entity.ExtendedPost;
import com.labEpam.timeCounter.entity.Values;
import com.labEpam.timeCounter.jpa.model.ValuesDB;
import com.labEpam.timeCounter.memory.MemoryStorage;
import com.labEpam.timeCounter.services.CalculateTime;
import com.labEpam.timeCounter.services.CounterService;
import com.labEpam.timeCounter.services.DataBaseService;
import com.labEpam.timeCounter.validators.ParameterValidationError;
import com.labEpam.timeCounter.validators.ParameterValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/time")
public class CalculateTimeController {
    private static final Logger logger = LogManager.getLogger(CalculateTimeController.class);
    private CalculateTime calculateTimeService;

    private final DataBaseService dataBaseService;
    private TimeAsync timeAsync;
    private ParameterValidator parameterValidator;
    private MemoryStorage memoryStorage = new MemoryStorage();
    private CounterService counterService = new CounterService();
    public CalculateTimeController(CalculateTime calculateTimeService, DataBaseService dataBaseService, TimeAsync timeAsync, ParameterValidator parameterValidator, MemoryStorage memoryStorage, CounterService counterService) {
        this.calculateTimeService = calculateTimeService;
        this.dataBaseService = dataBaseService;
        this.timeAsync = timeAsync;
        this.parameterValidator = parameterValidator;
        this.memoryStorage = memoryStorage;
        this.counterService = counterService;
    }
    @GetMapping(value ="/count")
    public ResponseEntity<Object> getTime(@RequestParam(name = "speed") Float speed,
                        @RequestParam(name = "distance") Float distance) {

        counterService.incrementSynchronisedCounter();
        counterService.incrementUnsynchronisedCounter();
        logger.info("Parameter validation");
        ParameterValidationError speedResponse = parameterValidator.validateSpeed(speed);
        ParameterValidationError distanceResponse = parameterValidator.validateDistance(distance);
        ParameterValidationError generalResponse = new ParameterValidationError();

        if(speedResponse.getMessageErrors().size() != 0) {
            logger.error("Speed parameter is not valid");
            speedResponse.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(speedResponse, HttpStatus.BAD_REQUEST);
        }

        if(distanceResponse.getMessageErrors().size() != 0) {
            logger.error("Distance parameter is not valid");
            distanceResponse.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(distanceResponse, HttpStatus.BAD_REQUEST);
        }
        try {
            logger.info("Counting results");
            Values time = new Values(speed, distance);
            float result = calculateTimeService.count(time.getSpeed(), time.getDistance());
            generalResponse = parameterValidator.validateTime(result);

            if (generalResponse.getMessageErrors().size() == 0) {
                time.setTime(result);
                memoryStorage.saveValues(time);
                dataBaseService.saveValue(time);
                logger.info("Calculation was made successfully");
                return ResponseEntity.ok(time);
            }
            else {
                logger.error("Result is not valid");
                generalResponse.setStatus(HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(generalResponse, HttpStatus.BAD_REQUEST);
            }
        }
        catch (RuntimeException exception) {
            return new ResponseEntity<>(generalResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "/counter")
    @ResponseStatus(HttpStatus.OK)
    public Counter getCounter() {

        return new Counter(counterService);
    }

    @GetMapping(value = "/values")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Values>> getAllValues() {

        return ResponseEntity.ok(memoryStorage.getAllValues());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> uncheckedError(RuntimeException exception) {
        ParameterValidationError response = new ParameterValidationError();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.addMessageError("Error 500: " + RuntimeException.class);
        logger.error("Error 500");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/countBulk")
    @ResponseStatus(HttpStatus.CREATED)
    public ExtendedPost postTime(@RequestBody List<BulkValues> values) {

        logger.info("Bulk operation with filters");
        parameterValidator.validateBulkOperation(values);

        ExtendedPost results = new ExtendedPost();
        results.setResult(calculateTimeService.countBulk(values));

        memoryStorage.saveValues(results.getResult());
        dataBaseService.saveAll(results.getResult());

        results.setMaxTime(calculateTimeService.maxTime(results.getResult()));
        results.setMinTime(calculateTimeService.minTime(results.getResult()));
        results.setAverageTime(calculateTimeService.averageTime(results.getResult()));

        return results;
    }

    @PostMapping("/async")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer asyncTime(@RequestParam(name = "speed") Float speed,
                             @RequestParam(name = "distance") Float distance) {

        logger.info("Async call");
        parameterValidator.validateAsyncCall(speed, distance);

        ValuesDB valuesDB = new ValuesDB(speed, distance);
        int id = timeAsync.createAsync(valuesDB);
        timeAsync.computeAsync(id);

        return id;
    }

    @GetMapping("/result/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ValuesDB resultId(@PathVariable("id") int id) {

        logger.info("Finding object in the DB by ID");
        return dataBaseService.findOne(id);
    }
}

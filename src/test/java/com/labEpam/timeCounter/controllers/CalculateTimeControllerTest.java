package com.labEpam.timeCounter.controllers;

import com.labEpam.timeCounter.entity.BulkValues;
import com.labEpam.timeCounter.entity.Counter;
import com.labEpam.timeCounter.entity.ExtendedPost;
import com.labEpam.timeCounter.entity.Values;
import com.labEpam.timeCounter.memory.MemoryStorage;
import com.labEpam.timeCounter.services.CalculateTime;
import com.labEpam.timeCounter.services.CounterService;
import com.labEpam.timeCounter.validators.ParameterValidationError;
import com.labEpam.timeCounter.validators.ParameterValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
public class CalculateTimeControllerTest {

    @Mock
    private ParameterValidator parameterValidator;

    @Mock
    private CalculateTime calculateTime;

    @Mock
    private MemoryStorage memoryStorage;

    @Mock
    private CounterService counterService;

    @InjectMocks
    private CalculateTimeController calculateTimeController;

    @Test
    public void testGetTimeWithValidParameters() {
        float speed = 50.0f;
        float distance = 100.0f;
        Values expectedTime = new Values(speed, distance);
        expectedTime.setTime(2.0f);
        ParameterValidationError validationResult = new ParameterValidationError();
        when(parameterValidator.validateSpeed(speed)).thenReturn(validationResult);
        when(parameterValidator.validateDistance(distance)).thenReturn(validationResult);
        when(calculateTime.count(speed, distance)).thenReturn(2.0f);
        when(parameterValidator.validateTime(2.0f)).thenReturn(validationResult);
        when(memoryStorage.checkPut(expectedTime)).thenReturn(false);

        ResponseEntity<Object> response = calculateTimeController.getTime(speed, distance);
        memoryStorage.saveValues((Values) response.getBody());
        boolean success = memoryStorage.checkPut((Values) response.getBody());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody() instanceof Values);
        Values actualTime = (Values) response.getBody();
        Assertions.assertEquals(expectedTime.getSpeed(), actualTime.getSpeed(), 0.0f);
        Assertions.assertEquals(expectedTime.getDistance(), actualTime.getDistance(), 0.0f);
        Assertions.assertEquals(expectedTime.getTime(), actualTime.getTime(), 0.0f);
        Assertions.assertFalse(success);
        // verify(memoryStorage).saveValues(actualTime);
    }

    @Test
    public void testGetTimeWithInvalidSpeedParameter() {
        float speed = -50.0f;
        float distance = 100.0f;
        ParameterValidationError validationResult = new ParameterValidationError();
        validationResult.getMessageErrors().add("Invalid speed parameter");
        when(parameterValidator.validateSpeed(speed)).thenReturn(validationResult);

        ResponseEntity<Object> response = calculateTimeController.getTime(speed, distance);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertTrue(response.getBody() instanceof ParameterValidationError);
        ParameterValidationError actualResponse = (ParameterValidationError) response.getBody();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatus());
        Assertions.assertEquals(validationResult.getMessageErrors(), actualResponse.getMessageErrors());
    }

    @Test
    public void testGetTimeWithInvalidDistanceParameter() {
        float speed = 50.0f;
        float distance = -100.0f;
        ParameterValidationError validationResult = new ParameterValidationError();
        validationResult.getMessageErrors().add("Invalid distance parameter");
        when(parameterValidator.validateSpeed(speed)).thenReturn(validationResult);
        when(parameterValidator.validateDistance(distance)).thenReturn(validationResult);

        ResponseEntity<Object> response = calculateTimeController.getTime(speed, distance);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertTrue(response.getBody() instanceof ParameterValidationError);
        ParameterValidationError actualResponse = (ParameterValidationError) response.getBody();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatus());
        Assertions.assertEquals(validationResult.getMessageErrors(), actualResponse.getMessageErrors());
    }

    @Test
    public void testGetTimeWithInvalidResult() {
        float speed = 50.0f;
        float distance = 100.0f;
        Values expectedTime = new Values(speed, distance);
        ParameterValidationError validationResult = new ParameterValidationError();
        validationResult.getMessageErrors().add("Invalid result");
        when(parameterValidator.validateSpeed(speed)).thenReturn(validationResult);
        when(parameterValidator.validateDistance(distance)).thenReturn(validationResult);
        when(calculateTime.count(speed, distance)).thenReturn(0.0f);
        when(parameterValidator.validateTime(0.0f)).thenReturn(validationResult);

        ResponseEntity<Object> response = calculateTimeController.getTime(speed, distance);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertTrue(response.getBody() instanceof ParameterValidationError);
    }

    @Test
    void testUncheckedError() {
        RuntimeException exception = new RuntimeException("Test Exception");
        ParameterValidationError expectedResponse = new ParameterValidationError();
        expectedResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        expectedResponse.addMessageError("Error 500: " + RuntimeException.class);

        ResponseEntity<Object> actualResponse = calculateTimeController.uncheckedError(exception);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualResponse.getStatusCode());
        Assertions.assertTrue(actualResponse.getBody() instanceof ParameterValidationError);
    }

    @Test
    public void testPutMemoryStorage() {
        float speed = 50.0f;
        float distance = 100.0f;
        Values expectedTime = new Values(speed, distance);
        ParameterValidationError validationResult = new ParameterValidationError();
        validationResult.getMessageErrors().add("Results were not saved to memory storage");
        when(memoryStorage.checkPut(expectedTime)).thenReturn(false);

        memoryStorage.saveValues(expectedTime);
        boolean response = memoryStorage.checkPut(expectedTime);

        Assertions.assertFalse(response);
    }

    @Test
    public void testGetAllValues() {
        List<Values> expectedValues = new ArrayList<>();
        expectedValues.add(new Values(50.0f, 1000.0f, 20.0f));
        expectedValues.add(new Values(40.0f, 500.0f, 12.5f));
        expectedValues.add(new Values(60.0f, 1800.0f, 30.0f));
        when(memoryStorage.getAllValues()).thenReturn(expectedValues);

        ResponseEntity<List<Values>> response = calculateTimeController.getAllValues();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Values> values = response.getBody();
        Assertions.assertEquals(3, values.size());
        Assertions.assertEquals(50.0f, values.get(0).getSpeed());
        Assertions.assertEquals(1000.0f, values.get(0).getDistance());
        Assertions.assertEquals(20.0f, values.get(0).getTime());
        Assertions.assertEquals(40.0f, values.get(1).getSpeed());
        Assertions.assertEquals(500.0f, values.get(1).getDistance());
        Assertions.assertEquals(12.5f, values.get(1).getTime());
        Assertions.assertEquals(60.0f, values.get(2).getSpeed());
        Assertions.assertEquals(1800.0f, values.get(2).getDistance());
        Assertions.assertEquals(30.0f, values.get(2).getTime());
    }

    @Test
    public void testGetCounter() {
        int expectedSynchronisedCount = 10;
        int expectedUnsynchronisedCount = 9;

        when(counterService.getSynchronisedCounter()).thenReturn(expectedSynchronisedCount);
        when(counterService.getUnsynchronisedCounter()).thenReturn(expectedUnsynchronisedCount);

        Counter result= calculateTimeController.getCounter();

        Assertions.assertEquals(expectedSynchronisedCount, result.getSynchCounter());
        Assertions.assertEquals(expectedUnsynchronisedCount, result.getUnsyncCounter());

        verify(counterService, times(1)).getSynchronisedCounter();
        verify(counterService, times(1)).getUnsynchronisedCounter();
    }

    @Test
    public void testPostTime() {
        List<BulkValues> values = new ArrayList<>();
        BulkValues bulkValueFirst = new BulkValues(5.0f, 10.0f);
        BulkValues bulkValueSecond = new BulkValues(6.0f, 12.0f);
        values.add(bulkValueFirst);
        values.add(bulkValueSecond);

        when(calculateTime.countBulk(values)).thenReturn(new ArrayList<>());
        when(calculateTime.maxTime(anyList())).thenReturn(5.0f);
        when(calculateTime.minTime(anyList())).thenReturn(1.0f);
        when(calculateTime.averageTime(anyList())).thenReturn(3.0f);

        ExtendedPost result = calculateTimeController.postTime(values);

        // Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
        verify(parameterValidator, times(1)).validateBulkOperation(values);
        verify(memoryStorage, times(1)).saveValues(anyList());
        Assertions.assertEquals(5.0f, result.getMaxTime());
        Assertions.assertEquals(1.0f, result.getMinTime());
        Assertions.assertEquals(3.0f, result.getAverageTime());
    }
}

package com.labEpam.timeCounter.validators;

import com.labEpam.timeCounter.entity.BulkValues;
import com.labEpam.timeCounter.entity.Values;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParameterValidator {
    private static final Logger logger = LogManager.getLogger(ParameterValidator.class);

    public ParameterValidationError validateDistance(Float distance) {
        ParameterValidationError response = new ParameterValidationError();
        if (distance <= 0) {
            logger.error("Distance value can't be zero or negative");
            response.addMessageError("Distance value can't be zero or negative");
        }
        return response;
    }
    public ParameterValidationError validateSpeed(Float speed) {
        ParameterValidationError response = new ParameterValidationError();
        if (speed <= 0) {
            logger.error("Speed value can't be zero or negative");
            response.addMessageError("Speed value can't be zero or negative");
        }
        if (speed > 400) {
            logger.error("Speed can't be more than 400 km/h");
            response.addMessageError("Speed can't be more than 400 km/h");
        }
        return response;
    }

    public ParameterValidationError validateTime(Float time) {
        ParameterValidationError response = new ParameterValidationError();
        if (time <= 0) {
            logger.error("Time value can't be zero or negative");
            response.addMessageError("Time value can't be zero or negative");
        }
        return response;
    }

    public ResponseEntity<Object> validateBulkOperation(List<BulkValues> values) {

        ParameterValidator parameterValidator = new ParameterValidator();
        logger.info("Bulk parameter validation");

        boolean hasError = values.stream().anyMatch(value -> {
            ParameterValidationError speedResponse = parameterValidator.validateSpeed(value.getSpeed());
            ParameterValidationError distanceResponse = parameterValidator.validateDistance(value.getDistance());

            if (speedResponse.getMessageErrors().size() != 0) {
                logger.error("Bulk speed parameter is not valid");
                speedResponse.setStatus(HttpStatus.BAD_REQUEST);
                value.setNull();
                return true;
            }

            if (distanceResponse.getMessageErrors().size() != 0) {
                logger.error("Bulk distance parameter is not valid");
                distanceResponse.setStatus(HttpStatus.BAD_REQUEST);
                value.setNull();
                return true;
            }

            return false;
        });

        if (hasError) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> validateAsyncCall(Float speed, Float distance) {

        ParameterValidator parameterValidator = new ParameterValidator();
        logger.info("Async call parameter validation");
        ParameterValidationError speedResponse = parameterValidator.validateSpeed(speed);
        ParameterValidationError distanceResponse = parameterValidator.validateDistance(distance);

        if(speedResponse.getMessageErrors().size() != 0) {
            logger.error("Async call speed parameter is not valid");
            speedResponse.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(speedResponse, HttpStatus.BAD_REQUEST);
        }

        if(distanceResponse.getMessageErrors().size() != 0) {
            logger.error("Async call distance parameter is not valid");
            distanceResponse.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(distanceResponse, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

package com.labEpam.timeCounter.validators;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ParameterValidationError {
    private HttpStatus status;
    private List<String> messageErrors = new ArrayList<String>();
    public void setStatus(HttpStatus status){
        this.status = status;
    }

    public ParameterValidationError() {}
    public ParameterValidationError(String messageErrors, HttpStatus status) {
        this.status  = status;
        this.messageErrors.add(messageErrors);
    }

    public HttpStatus getStatus() {
        return status;
    }
    public void addMessageError(String messageError) {
        this.messageErrors.add(messageError);
    }

    public List<String> getMessageErrors() {
        return messageErrors;
    }
}

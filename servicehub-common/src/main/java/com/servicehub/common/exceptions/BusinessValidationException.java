package com.servicehub.common.exceptions;

import org.springframework.http.HttpStatus;

public class BusinessValidationException extends ServiceHubException{
    public BusinessValidationException(HttpStatus httpStatus, String errorCode, String message) {
        super(httpStatus, errorCode, message);
    }
}

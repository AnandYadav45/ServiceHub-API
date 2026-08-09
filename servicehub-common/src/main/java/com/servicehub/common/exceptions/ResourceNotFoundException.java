package com.servicehub.common.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ServiceHubException{
    public ResourceNotFoundException(HttpStatus httpStatus, String errorCode, String message) {
        super(httpStatus, errorCode, message);
    }
}

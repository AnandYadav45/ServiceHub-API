package com.servicehub.common.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends ServiceHubException{
    public DuplicateResourceException(HttpStatus httpStatus, String errorCode, String message) {
        super(httpStatus, errorCode, message);
    }
}

package com.servicehub.common.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends  ServiceHubException{
    public UnauthorizedException(HttpStatus httpStatus, String errorCode, String message) {
        super(httpStatus, errorCode, message);
    }
}

package com.servicehub.common.exceptions;

import org.springframework.http.HttpStatus;

public class DownstreamServiceException extends ServiceHubException{
    public DownstreamServiceException(HttpStatus httpStatus, String errorCode, String message) {
        super(httpStatus, errorCode, message);
    }
}

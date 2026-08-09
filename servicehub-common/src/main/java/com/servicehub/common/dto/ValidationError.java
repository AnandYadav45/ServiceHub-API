package com.servicehub.common.dto;

public record ValidationError(
        String field,
        Object rejectedValue,
        String message
) {
}


package com.servicehub.common.dto;

import java.util.List;

public record ApiError(
        String code,
        String message,
        List<FieldError> details
) {
    public record FieldError(String field, String message) {}
}

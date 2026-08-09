package com.servicehub.common.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private ApiError error;

    @Builder.Default
    private Instant timestamp = Instant.now();

    public static <T> ApiResponse<T> success(T data){
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }


    public static <T> ApiResponse<T> error(ApiError error) {
        return ApiResponse.<T>builder().success(false).error(error).build();
    }

    // Convenience overload for simple cases with no field-level details — keeps the
    // JwtAuthenticationFilter / entry-point code from a couple of turns ago compiling, just updated
    // to the (code, message) shape instead of a bare string.
    public static <T> ApiResponse<T> error(String code, String message) {
        return error(new ApiError(code, message, null));
    }

}

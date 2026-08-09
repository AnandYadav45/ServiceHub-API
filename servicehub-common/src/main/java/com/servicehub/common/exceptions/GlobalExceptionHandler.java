package com.servicehub.common.exceptions;

import com.servicehub.common.dto.ApiError;
import com.servicehub.common.dto.ApiResponse;
import com.servicehub.common.logging.CorrelationIdFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ServiceHubException.class)
    public ResponseEntity<ApiResponse<Void>> handleServiceHubException(ServiceHubException ex, HttpServletRequest request){
        return build(ex.getHttpStatus(), ex.getErrorCode(), ex.getMessage(), null);

    }

    // @Valid failures on @RequestBody DTOs — this is what makes your exact example shape real
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ApiError.FieldError> details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ApiError.FieldError(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());
        log.warn("Validation failed on {}: {}", request.getRequestURI(), details);
        return build(HttpStatus.BAD_REQUEST, "COMMON-400-001", "Validation failed", details);
    }

    // @Validated failures on @RequestParam / @PathVariable (different mechanism than @Valid @RequestBody)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        List<ApiError.FieldError> details = ex.getConstraintViolations().stream()
                .map(v -> new ApiError.FieldError(v.getPropertyPath().toString(), v.getMessage()))
                .collect(Collectors.toList());
        log.warn("Constraint violation on {}: {}", request.getRequestURI(), details);
        return build(HttpStatus.BAD_REQUEST, "COMMON-400-001", "Validation failed", details);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnreadableBody(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("Malformed request body on {}", request.getRequestURI());
        return build(HttpStatus.BAD_REQUEST, "COMMON-400-002", "Request body could not be read", null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = String.format("Parameter '%s' should be of type %s", ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
        log.warn("Type mismatch on {}: {}", request.getRequestURI(), message);
        return build(HttpStatus.BAD_REQUEST, "COMMON-400-003", message, null);
    }

    // Catches what slips past application-level validation — e.g. a race condition on a
    // unique constraint between two concurrent requests
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.error("Data integrity violation on {}", request.getRequestURI(), ex);   // full stack trace server-side only
        return build(HttpStatus.CONFLICT, "COMMON-409-001", "The request conflicts with existing data", null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, "COMMON-405-001", ex.getMessage(), null);
    }

//    @ExceptionHandler(NoResourceFoundException.class)
//    public ResponseEntity<ApiResponse<Void>> handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request) {
//        return build(HttpStatus.NOT_FOUND, "COMMON-404-001", "The requested resource does not exist", null);
//    }

    // Safety net — RestAccessDeniedHandler (from the auth work) catches most of these earlier
    // in the filter chain; this covers anything that reaches this far regardless
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return build(HttpStatus.FORBIDDEN, "SECURITY-403-001", "Access denied", null);
    }

    // The final safety net. NEVER leak ex.getMessage() or a stack trace to the client here —
    // log it in full, server-side, tied to the correlation ID; return a generic message plus
    // that same ID so a user/support can reference it without ever seeing internals.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex, HttpServletRequest request) {
        String traceId = MDC.get(CorrelationIdFilter.MDC_KEY);
        log.error("Unhandled exception on {} [traceId={}]", request.getRequestURI(), traceId, ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-500-001",
                "An unexpected error occurred. Reference: " + traceId, null);
    }

    @ExceptionHandler(DownstreamServiceException.class)
    public ResponseEntity<ApiResponse<Void>> handleDownstreamServiceException(DownstreamServiceException ex, HttpServletRequest request) {
        log.error("[{}] Downstream call failed on {}: {}", ex.getErrorCode(), request.getRequestURI(), ex.getMessage(), ex.getCause());
        return build(ex.getHttpStatus(), ex.getErrorCode(), ex.getMessage(), null);
    }


    private ResponseEntity<ApiResponse<Void>> build(HttpStatus status, String code, String message,
                                                    List<ApiError.FieldError> details) {
        return ResponseEntity.status(status).body(ApiResponse.error(new ApiError(code, message, details)));
    }
}

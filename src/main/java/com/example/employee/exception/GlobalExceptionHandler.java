package com.example.employee.exception;

import com.example.employee.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("Resource not found: {}", ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleDatabaseException(DatabaseException ex, HttpServletRequest request) {
        log.error("Database error occurred: {}", ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleDataAccessException(DataAccessException ex, HttpServletRequest request) {
        log.error("Data access error: ", ex);
        ApiResponse<Map<String, Object>> errorResponse = ApiResponse.<Map<String, Object>>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .data(Map.of(
                        "error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        "message", "An error occurred while accessing the database. Please check if the data violates constraints.",
                        "path", request.getRequestURI()
                ))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        log.warn("Validation failed: {}", message);
        ApiResponse<Map<String, Object>> errorResponse = ApiResponse.<Map<String, Object>>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .data(Map.of(
                        "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        "message", message,
                        "path", request.getRequestURI()
                ))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleGlobalException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error occurred: ", ex);
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ApiResponse<Map<String, Object>>> buildErrorResponse(Exception ex, HttpStatus status, HttpServletRequest request) {
        ApiResponse<Map<String, Object>> errorResponse = ApiResponse.<Map<String, Object>>builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .data(Map.of(
                        "error", status.getReasonPhrase(),
                        "message", ex.getMessage() != null ? ex.getMessage() : "Unknown error",
                        "path", request.getRequestURI()
                ))
                .build();
        return new ResponseEntity<>(errorResponse, status);
    }
}

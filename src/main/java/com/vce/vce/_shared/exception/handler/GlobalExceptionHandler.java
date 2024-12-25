package com.vce.vce._shared.exception.handler;

import com.vce.vce._shared.exception.EntityAlreadyExistsException;
import com.vce.vce._shared.exception.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final HttpServletRequest request;

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFound(EntityNotFoundException ex) {
        return createErrorResponse(
            ex.getMessage(),
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value()
        );
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEntityExists(EntityAlreadyExistsException ex) {
        return createErrorResponse(
            ex.getMessage(),
            ex.getMessage(),
            HttpStatus.CONFLICT.value()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneral(Exception ex) {
        return createErrorResponse(
            "Internal server error",
            ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return createErrorResponse(
                "Validation failed",
                errors,
                HttpStatus.BAD_REQUEST.value()
        );
    }

    private ErrorResponse createErrorResponse(String type, String message, int statusCode) {
        return ErrorResponse.builder()
                .type(type)
                .message(message)
                .statusCode(statusCode)
                .path(request.getRequestURI())
                .build();
    }

    private ErrorResponse createErrorResponse(String type, List<String> errors, int statusCode) {
        return ErrorResponse.builder()
                .type(type)
                .errors(errors)
                .statusCode(statusCode)
                .path(request.getRequestURI())
                .build();
    }
}
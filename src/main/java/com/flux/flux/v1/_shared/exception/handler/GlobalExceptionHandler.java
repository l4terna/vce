package com.flux.flux.v1._shared.exception.handler;

import com.flux.flux.v1._shared.exception.EntityAlreadyExistsException;
import com.flux.flux.v1._shared.exception.ErrorResponse;
import com.flux.flux.v1._shared.exception.enumeration.ErrorType;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorController {
    private final HttpServletRequest request;

    @ExceptionHandler({
            EntityNotFoundException.class,
            NoResourceFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFound(Exception ex) {
        return createErrorResponse(
            ErrorType.NOT_FOUND,
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value()
        );
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEntityExists(EntityAlreadyExistsException ex) {
        return createErrorResponse(
            ErrorType.ALREADY_EXISTS,
            ex.getMessage(),
            HttpStatus.CONFLICT.value()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDenied(AccessDeniedException ex) {
        return createErrorResponse(
                ErrorType.ACCESS_DENIED,
                ex.getMessage(),
                HttpStatus.FORBIDDEN.value()
        );
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ValidationException.class,
            ConstraintViolationException.class,
            MethodArgumentTypeMismatchException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationErrors(Exception ex) {
        List<String> errors = null;

        if (ex instanceof MethodArgumentNotValidException validationEx) {
            errors = validationEx.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .toList();
        }
        else if (ex instanceof ValidationException) {
            errors = List.of(ex.getMessage());
        }
        else {
            return createErrorResponse(
                    ErrorType.VALIDATION_ERROR,
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST.value()
            );
        }

        return createErrorResponse(
                ErrorType.VALIDATION_ERROR,
                errors,
                HttpStatus.BAD_REQUEST.value()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneral(Exception ex) {
        return createErrorResponse(
                ErrorType.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
    }

    private ErrorResponse createErrorResponse(ErrorType type, String message, int statusCode) {
        return ErrorResponse.builder()
                .type(type)
                .message(message)
                .statusCode(statusCode)
                .path(request.getRequestURI())
                .build();
    }

    private ErrorResponse createErrorResponse(ErrorType type, List<String> errors, int statusCode) {
        return ErrorResponse.builder()
                .type(type)
                .errors(errors)
                .statusCode(statusCode)
                .path(request.getRequestURI())
                .build();
    }
}
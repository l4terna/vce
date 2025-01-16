package com.flux.flux.v1._shared.exception;

import com.flux.flux.v1._shared.exception.enumeration.ErrorType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ErrorResponse(
    String message,
    ErrorType type,
    int statusCode,
    LocalDateTime timestamp,
    String path,
    List<String> errors
) {}
package com.vce.vce._shared.exception;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ErrorResponse(
    String message,
    String type,
    int statusCode,
    LocalDateTime timestamp,
    String path,
    List<String> errors
) {}
package io.teamchallenge.mentality.exception.dto;

import java.time.LocalDateTime;

public record ApiErrorResponse(
    LocalDateTime timestamp,
    String httpStatus,
    Integer httpStatusCode,
    String errorMessage,
    String path) {}

package io.teamchallenge.mentality.exception.dto;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public record ApiErrorResponse(
    LocalDateTime timestamp,
    String httpStatus,
    Integer httpStatusCode,
    String errorMessage,
    String path) {

  public ApiErrorResponse(HttpStatus status, String errorMessage, String path) {
    this(
        LocalDateTime.now(), status.name(), status.value(), errorMessage, path.replace("uri=", ""));
  }
}

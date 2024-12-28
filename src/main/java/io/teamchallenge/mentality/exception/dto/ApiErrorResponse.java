package io.teamchallenge.mentality.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;

public record ApiErrorResponse(
    LocalDateTime timestamp,
    String httpStatus,
    Integer httpStatusCode,
    String errorMessage,
    String path,
    @JsonInclude(JsonInclude.Include.NON_NULL) List<String> errorDetails) {

  public ApiErrorResponse(HttpStatus status, String errorMessage, String path) {
    this(
        LocalDateTime.now(),
        status.name(),
        status.value(),
        errorMessage,
        path.replace("uri=", ""),
        null);
  }

  public ApiErrorResponse(
      HttpStatus status, String errorMessage, String path, List<String> errorDetails) {
    this(
        LocalDateTime.now(),
        status.name(),
        status.value(),
        errorMessage,
        path.replace("uri=", ""),
        errorDetails);
  }
}

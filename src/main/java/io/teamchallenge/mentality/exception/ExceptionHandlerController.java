package io.teamchallenge.mentality.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import io.teamchallenge.mentality.exception.dto.ApiErrorResponse;
import java.util.ArrayList;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      @NonNull MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    ApiErrorResponse resp =
        new ApiErrorResponse(
            HttpStatus.valueOf(status.value()),
            "Request validation failed",
            request.getDescription(false),
            new ArrayList<>());
    var errorDetails = resp.errorDetails();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            error ->
                errorDetails.add(
                    "[%s]: %s. Rejected value: [%s]"
                        .formatted(
                            error.getField(),
                            error.getDefaultMessage(),
                            error.getRejectedValue())));
    return ResponseEntity.status(status).contentType(MediaType.APPLICATION_PROBLEM_JSON).body(resp);
  }

  @ExceptionHandler(GoogleTokenException.class)
  public ResponseEntity<Object> handleGoogleTokenException(
      final GoogleTokenException e, final WebRequest req) {
    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(
            new ApiErrorResponse(INTERNAL_SERVER_ERROR, e.getMessage(), req.getDescription(false)));
  }

  @ExceptionHandler({InvalidGoogleTokenException.class, GithubTokenException.class})
  public ResponseEntity<Object> handleInvalidTokenException(
      final RuntimeException e, final WebRequest req) {
    return ResponseEntity.status(UNAUTHORIZED)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(new ApiErrorResponse(UNAUTHORIZED, e.getMessage(), req.getDescription(false)));
  }

  @ExceptionHandler({CustomerNotFoundException.class, ProductNotFoundException.class})
  public ResponseEntity<Object> handleEntityNotFoundException(
      final RuntimeException e, final WebRequest req) {
    return ResponseEntity.status(NOT_FOUND)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(new ApiErrorResponse(NOT_FOUND, e.getMessage(), req.getDescription(false)));
  }

  @ExceptionHandler(JsonException.class)
  public ResponseEntity<Object> handleJsonException(final JsonException e, final WebRequest req) {
    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(
            new ApiErrorResponse(INTERNAL_SERVER_ERROR, e.getMessage(), req.getDescription(false)));
  }
}

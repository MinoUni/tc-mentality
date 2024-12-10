package io.teamchallenge.mentality.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import io.teamchallenge.mentality.exception.dto.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

  @ExceptionHandler(GoogleTokenException.class)
  public ResponseEntity<Object> handleGoogleTokenException(
      final GoogleTokenException e, final WebRequest req) {
    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .body(
            new ApiErrorResponse(INTERNAL_SERVER_ERROR, e.getMessage(), req.getDescription(false)));
  }

  @ExceptionHandler({InvalidGoogleTokenException.class, GithubTokenException.class})
  public ResponseEntity<Object> handleInvalidTokenException(
      final RuntimeException e, final WebRequest req) {
    return ResponseEntity.status(UNAUTHORIZED)
        .body(new ApiErrorResponse(UNAUTHORIZED, e.getMessage(), req.getDescription(false)));
  }

  @ExceptionHandler({CustomerNotFoundException.class})
  public ResponseEntity<Object> handleEntityNotFoundException(
      final RuntimeException e, final WebRequest req) {
    return ResponseEntity.status(NOT_FOUND)
        .body(new ApiErrorResponse(NOT_FOUND, e.getMessage(), req.getDescription(false)));
  }
}

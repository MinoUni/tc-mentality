package io.teamchallenge.mentality.exception;

import io.teamchallenge.mentality.exception.dto.ApiErrorResponse;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

  @ExceptionHandler({GithubTokenException.class, IdTokenParseException.class})
  public ResponseEntity<Object> handleGithubTokenException(
      final GithubTokenException e, final WebRequest req) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.name(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                req.getDescription(false).replace("uri=", "")));
  }
}

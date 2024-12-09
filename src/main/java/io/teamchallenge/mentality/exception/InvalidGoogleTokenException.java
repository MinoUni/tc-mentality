package io.teamchallenge.mentality.exception;

public class InvalidGoogleTokenException extends RuntimeException {

  public InvalidGoogleTokenException() {
    super("Id token is invalid");
  }
}

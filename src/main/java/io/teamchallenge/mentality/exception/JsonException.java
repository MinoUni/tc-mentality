package io.teamchallenge.mentality.exception;

public class JsonException extends RuntimeException {

  public JsonException(Class<?> clazz, Integer id) {
    super("Failed to convert JSON node to `%s` with id=`%d`".formatted(clazz.getSimpleName(), id));
  }
}

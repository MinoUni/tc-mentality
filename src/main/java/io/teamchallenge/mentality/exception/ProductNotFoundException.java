package io.teamchallenge.mentality.exception;

public class ProductNotFoundException extends RuntimeException {

  public ProductNotFoundException(Integer id) {
    super("Product with id=%d not found".formatted(id));
  }
}

package io.teamchallenge.mentality.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductCategory {
  CARPETS(1);

  private final int id;
}

package io.teamchallenge.mentality.product.dto;

import io.teamchallenge.mentality.constraint.ValueOfEnum;
import io.teamchallenge.mentality.product.category.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import lombok.Value;

@Value
public class ProductDto {

  Integer id;
  String sku;
  @NotBlank String name;
  String description;
  @NotNull @Positive Integer quantityInStock;

  @ValueOfEnum(enumClass = ProductCategory.class)
  String category;

  @NotNull @Positive BigDecimal priceAmount;

  @NotBlank
  @Size(min = 3, max = 3)
  String priceCurrency;

  List<String> imagesUrls;

  public Integer id() {
    return id;
  }

  public String sku() {
    return sku;
  }

  public String name() {
    return name;
  }

  public String description() {
    return description;
  }

  public Integer quantityInStock() {
    return quantityInStock;
  }

  public String category() {
    return category;
  }

  public BigDecimal priceAmount() {
    return priceAmount;
  }

  public String priceCurrency() {
    return priceCurrency;
  }

  public List<String> imagesUrls() {
    return imagesUrls;
  }
}

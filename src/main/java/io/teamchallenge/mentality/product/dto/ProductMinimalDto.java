package io.teamchallenge.mentality.product.dto;

import io.teamchallenge.mentality.product.category.ProductCategory;
import java.math.BigDecimal;
import java.util.List;

public record ProductMinimalDto(
    Integer id,
    String name,
    ProductCategory category,
    BigDecimal priceAmount,
    String priceCurrency,
    Integer quantityInStock,
    List<String> imagesUrls) {}

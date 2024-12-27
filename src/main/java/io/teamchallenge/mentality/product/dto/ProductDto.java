package io.teamchallenge.mentality.product.dto;

import io.teamchallenge.mentality.product.category.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public record ProductDto(
    Integer id,
    String sku,
    @NotBlank String name,
    String description,
    @NotNull @Positive Integer quantityInStock,
    ProductCategory category,
    @NotNull @Positive BigDecimal priceAmount,
    @NotBlank @Size(min = 3, max = 3) String priceCurrency,
    List<String> imagesUrls) {}

package io.teamchallenge.mentality.product.dto;

import io.teamchallenge.mentality.constraint.ValueOfEnum;
import io.teamchallenge.mentality.product.Product;
import io.teamchallenge.mentality.product.category.ProductCategory;
import jakarta.persistence.criteria.JoinType;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import org.javamoney.moneta.Money;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public record ProductFilter(
    String nameContain,
    @ValueOfEnum(enumClass = ProductCategory.class) String category,
    @Positive BigDecimal minPrice,
    @Positive BigDecimal maxPrice) {

  private static final String CURRENCY = "USD";

  public Specification<Product> toSpecification() {
    return Specification.where(nameContainsSpecification())
        .and(categorySpecification())
        .and(priceBetweenSpecification());
  }

  private Specification<Product> nameContainsSpecification() {
    return (root, query, criteriaBuilder) ->
        StringUtils.hasText(nameContain)
            ? criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")), "%" + nameContain.toLowerCase() + "%")
            : null;
  }

  private Specification<Product> categorySpecification() {
    return (root, query, criteriaBuilder) -> {
      if (category == null) {
        return null;
      }
      root.fetch("category", JoinType.LEFT);
      return criteriaBuilder.equal(root.get("category").get("name"), category.toUpperCase());
    };
  }

  private Specification<Product> priceBetweenSpecification() {
    return (root, query, criteriaBuilder) -> {
      if (minPrice != null && maxPrice != null) {
        Money min = Money.of(minPrice, CURRENCY);
        Money max = Money.of(maxPrice, CURRENCY);
        return criteriaBuilder.between(root.get("price"), min, max);
      }
      return null;
    };
  }
}

package io.teamchallenge.mentality.product;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import io.teamchallenge.mentality.product.category.Category;
import io.teamchallenge.mentality.product.dto.ProductDto;
import io.teamchallenge.mentality.product.dto.ProductMinimalDto;
import org.javamoney.moneta.Money;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    imports = {Money.class})
public interface ProductMapper {

  @Named("getCategoryByName")
  default String getCategoryByName(Category category) {
    return category.getName();
  }

  @Mapping(target = "category", qualifiedByName = "getCategoryByName", source = "category")
  ProductDto toProductDto(Product product);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "sku", ignore = true)
  @Mapping(target = "category", ignore = true)
  @Mapping(
      target = "price",
      expression = "java(Money.of(productDto.priceAmount(), productDto.priceCurrency()))")
  Product toEntity(ProductDto productDto);

  @Mapping(target = "category", qualifiedByName = "getCategoryByName", source = "category")
  ProductMinimalDto toProductMinimalDto(Product product);
}

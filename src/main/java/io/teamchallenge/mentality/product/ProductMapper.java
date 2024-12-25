package io.teamchallenge.mentality.product;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import io.teamchallenge.mentality.product.dto.ProductDto;
import org.javamoney.moneta.Money;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    imports = {Money.class})
public interface ProductMapper {

  @Mapping(
      target = "category",
      expression = "java(ProductCategory.valueOf(product.getCategory().getName()))")
  ProductDto toProductDto(Product product);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "sku", ignore = true)
  @Mapping(target = "category", ignore = true)
  @Mapping(
      target = "price",
      expression = "java(Money.of(productDto.priceAmount(), productDto.priceCurrency()))")
  Product toEntity(ProductDto productDto);
}

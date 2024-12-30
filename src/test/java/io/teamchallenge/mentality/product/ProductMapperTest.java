package io.teamchallenge.mentality.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.teamchallenge.mentality.product.category.Category;
import io.teamchallenge.mentality.product.category.ProductCategory;
import io.teamchallenge.mentality.product.dto.ProductDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ProductMapperTest {

  private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

  @Test
  void shouldMapProductToProductDto() {
    Product product =
        Product.builder()
            .name("Black Carpet")
            .sku(UUID.randomUUID().toString())
            .description("Description")
            .quantityInStock(100)
            .price(Money.of(425.25, "USD"))
            .imagesUrls(List.of("/image_1.jpg", "/image_2.jpg"))
            .build()
            .setCategory(Category.builder().id(1).name("CARPETS").build());

    ProductDto dto = mapper.toProductDto(product);

    assertEquals(product.getId(), dto.id());
    assertEquals(product.getSku(), dto.sku());
    assertEquals(product.getName(), dto.name());
    assertEquals(product.getDescription(), dto.description());
    assertEquals(product.getCategory().getName(), dto.category());
    assertEquals(product.getQuantityInStock(), dto.quantityInStock());
    assertEquals(0, product.getPriceAmount().compareTo(dto.priceAmount()));
    assertEquals(product.getPriceCurrency(), dto.priceCurrency());
    assertEquals(product.getImagesUrls(), dto.imagesUrls());
  }

  @Test
  void shouldMapProductDtoToEntity() {
    ProductDto productDto =
        new ProductDto(
            1,
            UUID.randomUUID().toString(),
            "Black Carpet",
            "Description",
            100,
            ProductCategory.CARPETS.name(),
            BigDecimal.valueOf(425.25),
            "USD",
            List.of("/image_1.jpg", "/image_2.jpg"));

    Product product = mapper.toEntity(productDto);

    assertNull(product.getId());
    assertNull(product.getSku());
    assertEquals(productDto.name(), product.getName());
    assertEquals(productDto.description(), product.getDescription());
    assertEquals(productDto.quantityInStock(), product.getQuantityInStock());
    assertNull(product.getCategory());
    assertEquals(0, productDto.priceAmount().compareTo(product.getPriceAmount()));
    assertEquals(productDto.priceCurrency(), product.getPriceCurrency());
    assertEquals(productDto.imagesUrls(), product.getImagesUrls());
  }

  @Test
  void updateWithNull_shouldUpdateProductWithProductDtoSkippingNull() {
    ProductDto productDto =
        new ProductDto(
            2,
            UUID.randomUUID().toString(),
            "Black Carpet",
            "Description",
            100,
            ProductCategory.CARPETS.name(),
            BigDecimal.valueOf(425.25),
            "USD",
            List.of("/image_3.jpg", "/image_4.jpg"));
    Product product =
        Product.builder()
            .id(1)
            .sku(UUID.randomUUID().toString())
            .name("BC")
            .description("D")
            .quantityInStock(1)
            .price(Money.of(89, "USD"))
            .imagesUrls(new ArrayList<>(List.of("/image_1.jpg", "/image_2.jpg")))
            .build();

    mapper.updateWithNull(productDto, product);

    assertNotEquals(productDto.id(), product.getId());
    assertNotEquals(productDto.sku(), product.getSku());
    assertEquals(productDto.name(), product.getName());
    assertEquals(productDto.description(), product.getDescription());
    assertEquals(productDto.quantityInStock(), product.getQuantityInStock());
    assertNull(product.getCategory());
    assertEquals(0, productDto.priceAmount().compareTo(product.getPriceAmount()));
    assertEquals(productDto.priceCurrency(), product.getPriceCurrency());
    assertEquals(4, product.getImagesUrls().size());
  }
}

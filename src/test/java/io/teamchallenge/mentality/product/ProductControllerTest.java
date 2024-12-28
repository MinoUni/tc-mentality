package io.teamchallenge.mentality.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import io.teamchallenge.mentality.product.category.ProductCategory;
import io.teamchallenge.mentality.product.dto.ProductDto;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
@Sql(
    scripts = "classpath:/product/insert-products.sql",
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(
    scripts = "classpath:/product/delete-products.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

  @LocalServerPort private Integer port;

  @Autowired private TestRestTemplate restTemplate;

  @Test
  void create_shouldCreateNewProduct() {
    final int id = 2;
    final String expectedUrl = "http://localhost:%d/products/%d".formatted(port, id);
    ProductDto productDto =
        new ProductDto(
            null,
            null,
            "Black Carpet",
            "Description",
            100,
            ProductCategory.CARPETS.name(),
            BigDecimal.valueOf(425.25),
            "USD",
            List.of("/image_1.jpg", "/image_2.jpg"));

    String productUrl = restTemplate.postForLocation("/products", productDto).toString();

    assertEquals(expectedUrl, productUrl);
  }

  @Test
  void getOneById_shouldFindProductById() {
    final int id = 1;
    ProductDto expected =
        new ProductDto(
            id,
            null,
            "Yoga Carpet Majestic K60",
            "Product description",
            100,
            ProductCategory.CARPETS.name(),
            BigDecimal.valueOf(406.56),
            "USD",
            List.of(
                "https://io.ment.strg/products/car_majk60_1.png",
                "https://io.ment.strg/products/car_majk60_2.png",
                "https://io.ment.strg/products/car_majk60_3.png"));

    var resp = restTemplate.getForEntity("/products/{id}", ProductDto.class, id);

    assertEquals(HttpStatus.OK, resp.getStatusCode());
    assertEquals(APPLICATION_JSON, resp.getHeaders().getContentType());

    ProductDto productDto = resp.getBody();

    assertNotNull(productDto);
    assertEquals(id, productDto.id());
    assertNotNull(productDto.sku());
    assertEquals(expected.name(), productDto.name());
    assertEquals(expected.description(), productDto.description());
    assertEquals(expected.quantityInStock(), productDto.quantityInStock());
    assertEquals(expected.category(), productDto.category());
    assertEquals(0, expected.priceAmount().compareTo(productDto.priceAmount()));
    assertEquals(expected.priceCurrency(), productDto.priceCurrency());
    assertEquals(expected.imagesUrls(), productDto.imagesUrls());
  }
}

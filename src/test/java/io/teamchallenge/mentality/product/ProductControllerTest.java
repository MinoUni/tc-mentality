package io.teamchallenge.mentality.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import io.teamchallenge.mentality.product.category.ProductCategory;
import io.teamchallenge.mentality.product.dto.ProductDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
@Sql(
    scripts = "classpath:/product/insert_test-data.sql",
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(
    scripts = "classpath:/product/clean_test-data.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

  @LocalServerPort private Integer port;

  @Autowired private TestRestTemplate restTemplate;

  @Test
  @Order(1)
  void shouldCreateNewProduct() {
    final String expectedUrl = "http://localhost:%d/products/1".formatted(port);
    ProductDto productDto =
        new ProductDto(
            1,
            UUID.randomUUID().toString(),
            "Black Carpet",
            "Description",
            100,
            ProductCategory.CARPETS,
            BigDecimal.valueOf(425.25),
            "USD",
            List.of("/image_1.jpg", "/image_2.jpg"));

    String productUrl = restTemplate.postForLocation("/products", productDto).toString();

    assertEquals(expectedUrl, productUrl);
  }

  @Test
  @Order(2)
  void shouldGetOneById() {
    final int id = 1;
    ProductDto expected =
        new ProductDto(
            id,
            null,
            "Black Carpet",
            "Description",
            100,
            ProductCategory.CARPETS,
            BigDecimal.valueOf(425.25),
            "USD",
            List.of("/image_1.jpg", "/image_2.jpg"));

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

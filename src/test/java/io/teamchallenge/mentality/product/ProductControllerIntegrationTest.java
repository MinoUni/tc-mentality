package io.teamchallenge.mentality.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

import io.teamchallenge.mentality.exception.dto.ApiErrorResponse;
import io.teamchallenge.mentality.product.category.ProductCategory;
import io.teamchallenge.mentality.product.dto.ProductDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
class ProductControllerIntegrationTest {

  @LocalServerPort private Integer port;

  @Autowired private TestRestTemplate restTemplate;

  @Test
  void create_shouldCreateNewProduct() {
    final int id = 4;
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

  @Test
  void delete_shouldDeleteAndReturnNoContentResponse() {
    final int id = 2;

    var resp = restTemplate.exchange("/products/{id}", HttpMethod.DELETE, null, String.class, id);

    assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
  }

  @Test
  void delete_shouldReturnNotFoundResponse() {
    HttpStatus notFound = HttpStatus.NOT_FOUND;
    final int id = 15;

    var resp =
        restTemplate.exchange(
            "/products/{id}", HttpMethod.DELETE, null, ApiErrorResponse.class, id);
    ApiErrorResponse errorResp = resp.getBody();

    assertEquals(notFound, resp.getStatusCode());
    assertEquals(APPLICATION_PROBLEM_JSON, resp.getHeaders().getContentType());
    assertNotNull(errorResp);
    assertEquals(notFound.value(), errorResp.httpStatusCode());
    assertEquals(notFound.name(), errorResp.httpStatus());
    assertEquals("Product with id=%d not found".formatted(id), errorResp.errorMessage());
    assertEquals("/products/%d".formatted(id), errorResp.path());
  }

  @Test
  void update_shouldReturnUpdateProductDto() {
    final int id = 3;
    ProductDto productDtoReq =
        new ProductDto(
            15,
            UUID.randomUUID().toString(),
            "Yoga Carpet Majestic K160",
            "Product description",
            16,
            ProductCategory.CARPETS.name(),
            BigDecimal.valueOf(16.16),
            "USD",
            List.of("https://io.ment.strg/products/car_majk60_1.png"));

    var resp =
        restTemplate.exchange(
            "/products/{id}",
            HttpMethod.PUT,
            new HttpEntity<>(productDtoReq),
            ProductDto.class,
            id);
    ProductDto productDtoResp = resp.getBody();

    assertEquals(HttpStatus.OK, resp.getStatusCode());
    assertEquals(APPLICATION_JSON, resp.getHeaders().getContentType());
    assertNotNull(productDtoResp);
    assertEquals(id, productDtoResp.id());
    assertNotNull(productDtoResp.sku());
    assertEquals(productDtoReq.name(), productDtoResp.name());
    assertEquals(productDtoReq.description(), productDtoResp.description());
    assertEquals(productDtoReq.quantityInStock(), productDtoResp.quantityInStock());
    assertEquals(productDtoReq.category(), productDtoResp.category());
    assertEquals(0, productDtoReq.priceAmount().compareTo(productDtoResp.priceAmount()));
    assertEquals(productDtoReq.priceCurrency(), productDtoResp.priceCurrency());
    assertEquals(2, productDtoResp.imagesUrls().size());
  }

  @Test
  void update_shouldReturnProductNotFoundResponse() {
    HttpStatus notFound = HttpStatus.NOT_FOUND;
    final int id = 15;
    final ProductDto productDto =
        new ProductDto(
            id,
            null,
            "Yoga Carpet Majestic K60",
            "Product description",
            100,
            ProductCategory.CARPETS.name(),
            BigDecimal.valueOf(406.56),
            "USD",
            List.of("https://io.ment.strg/products/car_majk60_1.png"));

    var resp =
        restTemplate.exchange(
            "/products/{id}",
            HttpMethod.PUT,
            new HttpEntity<>(productDto),
            ApiErrorResponse.class,
            id);
    ApiErrorResponse errorResponse = resp.getBody();

    assertEquals(notFound, resp.getStatusCode());
    assertEquals(APPLICATION_PROBLEM_JSON, resp.getHeaders().getContentType());
    assertNotNull(errorResponse);
    assertEquals(notFound.value(), errorResponse.httpStatusCode());
    assertEquals(notFound.name(), errorResponse.httpStatus());
    assertEquals("Product with id=%d not found".formatted(id), errorResponse.errorMessage());
    assertEquals("/products/%d".formatted(id), errorResponse.path());
  }
}

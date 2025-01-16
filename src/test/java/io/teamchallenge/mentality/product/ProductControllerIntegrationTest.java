package io.teamchallenge.mentality.product;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.teamchallenge.mentality.product.category.ProductCategory;
import io.teamchallenge.mentality.product.dto.ProductDto;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@Sql(
    scripts = "classpath:/product/insert-products.sql",
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(
    scripts = "classpath:/product/delete-products.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

  @Autowired private WebApplicationContext context;

  private MockMvc mockMvc;

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  @WithAnonymousUser
  void create_shouldCreateNewProduct() throws Exception {
    final String productJson =
        """
          {
            "name": "Black Carpet",
            "description": "Very nice carpet",
            "quantityInStock": 100,
            "category": "CARPETS",
            "priceAmount": 425.25,
            "priceCurrency": "USD",
            "imagesUrls": [
              "/image_1.jpg",
              "/image_2.jpg"
            ]
          }
        """;
    mockMvc
        .perform(post("/products").contentType(APPLICATION_JSON).content(productJson))
        .andExpect(status().isCreated())
        .andExpect(header().exists(HttpHeaders.LOCATION));
  }

  @Test
  @WithAnonymousUser
  void getOneById_shouldFindProductById() throws Exception {
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

    mockMvc
        .perform(get("/products/{id}", id))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(expected.id()))
        .andExpect(jsonPath("$.sku").exists())
        .andExpect(jsonPath("$.name").value(expected.name()))
        .andExpect(jsonPath("$.description").value(expected.description()))
        .andExpect(jsonPath("$.quantityInStock").value(expected.quantityInStock()))
        .andExpect(jsonPath("$.category").value(expected.category()))
        .andExpect(jsonPath("$.priceAmount").value(expected.priceAmount()))
        .andExpect(jsonPath("$.priceCurrency").value(expected.priceCurrency()))
        .andExpect(jsonPath("$.imagesUrls", hasSize(expected.imagesUrls().size())));
  }

  @Test
  @WithMockUser
  void delete_shouldDeleteAndReturnNoContent() throws Exception {
    final int id = 2;
    mockMvc.perform(delete("/products/{id}", id)).andExpect(status().isNoContent());
  }

  @Test
  @WithAnonymousUser
  void delete_shouldReturnUnauthorized() throws Exception {
    final int id = 2;
    mockMvc.perform(delete("/products/{id}", id)).andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void delete_shouldReturnNotFound() throws Exception {
    final HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    final int id = 15;

    mockMvc
        .perform(delete("/products/{id}", id))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("timestamp").exists())
        .andExpect(jsonPath("httpStatus").value(httpStatus.name()))
        .andExpect(jsonPath("httpStatusCode").value(httpStatus.value()))
        .andExpect(jsonPath("errorMessage").value("Product with id=%d not found".formatted(id)))
        .andExpect(jsonPath("path").value("/products/%d".formatted(id)))
        .andExpect(jsonPath("errorDetails").doesNotExist());
  }

  @Test
  @WithMockUser
  void update_shouldUpdateAndReturnProductDto() throws Exception {
    final int id = 3;
    final String reqBody =
        """
          {
            "name": "Yoga Carpet Majestic K160",
            "description": "Product description",
            "quantityInStock": 16,
            "category": "CARPETS",
            "priceAmount": 16.16,
            "priceCurrency": "USD",
            "imagesUrls": [
              "https://io.ment.strg/products/car_majk60_1.png"
            ]
          }
        """;

    mockMvc
        .perform(put("/products/{id}", id).contentType(APPLICATION_JSON).content(reqBody))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("id").value(id))
        .andExpect(jsonPath("sku").exists())
        .andExpect(jsonPath("name").value("Yoga Carpet Majestic K160"))
        .andExpect(jsonPath("description").value("Product description"))
        .andExpect(jsonPath("quantityInStock").value(16))
        .andExpect(jsonPath("category").value(ProductCategory.CARPETS.name()))
        .andExpect(jsonPath("priceAmount").value(16.16))
        .andExpect(jsonPath("priceCurrency").value("USD"))
        .andExpect(jsonPath("imagesUrls", hasSize(2)));
  }

  @Test
  @WithMockUser
  void update_shouldReturnProductNotFound() throws Exception {
    final HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    final int id = 15;
    final String reqBody =
        """
          {
            "name": "Yoga Carpet Majestic K160",
            "description": "Product description",
            "quantityInStock": 16,
            "category": "CARPETS",
            "priceAmount": 16.16,
            "priceCurrency": "USD",
            "imagesUrls": [
              "https://io.ment.strg/products/car_majk60_1.png"
            ]
          }
        """;

    mockMvc
        .perform(put("/products/{id}", id).contentType(APPLICATION_JSON).content(reqBody))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("timestamp").exists())
        .andExpect(jsonPath("httpStatus").value(httpStatus.name()))
        .andExpect(jsonPath("httpStatusCode").value(httpStatus.value()))
        .andExpect(jsonPath("errorMessage").value("Product with id=%d not found".formatted(id)))
        .andExpect(jsonPath("path").value("/products/%d".formatted(id)))
        .andExpect(jsonPath("errorDetails").doesNotExist());
  }

  @Test
  @WithAnonymousUser
  void update_shouldReturnUnauthorized() throws Exception {
    final int id = 3;
    final String reqBody =
        """
          {
            "name": "Yoga Carpet Majestic K160",
            "description": "Product description",
            "quantityInStock": 16,
            "category": "CARPETS",
            "priceAmount": 16.16,
            "priceCurrency": "USD",
            "imagesUrls": [
              "https://io.ment.strg/products/car_majk60_1.png"
            ]
          }
        """;
    mockMvc
        .perform(put("/products/{id}", id).contentType(APPLICATION_JSON).content(reqBody))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithAnonymousUser
  void patch_shouldReturnUnauthorized() throws Exception {
    final int id = 4;
    final String patchNode =
        """
          {
            "name": "K444 Carpet Patched",
            "description": "Changed Description",
            "quantityInStock": 333
          }
        """;
    mockMvc
        .perform(patch("/products/{id}", id).contentType(APPLICATION_JSON).content(patchNode))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void patch_shouldUpdateAndReturnProductDto() throws Exception {
    final int id = 4;
    final String patchNode =
        """
          {
            "name": "K444 Carpet Patched",
            "description": "Changed Description",
            "quantityInStock": 333
          }
        """;

    mockMvc
        .perform(patch("/products/{id}", id).contentType(APPLICATION_JSON).content(patchNode))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("id").value(id))
        .andExpect(jsonPath("sku").exists())
        .andExpect(jsonPath("name").value("K444 Carpet Patched"))
        .andExpect(jsonPath("description").value("Changed Description"))
        .andExpect(jsonPath("quantityInStock").value(333))
        .andExpect(jsonPath("category").value(ProductCategory.CARPETS.name()))
        .andExpect(jsonPath("priceAmount").value(33.33))
        .andExpect(jsonPath("priceCurrency").value("USD"))
        .andExpect(jsonPath("imagesUrls").isEmpty());
  }

  @Test
  @WithMockUser
  void patch_shouldReturnProductNotFound() throws Exception {
    final HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    final int id = 15;
    final String patchNode =
        """
          {
            "name": "K444 Carpet Patched",
            "description": "Changed Description",
            "quantityInStock": 333
          }
        """;

    mockMvc
        .perform(patch("/products/{id}", id).contentType(APPLICATION_JSON).content(patchNode))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("timestamp").exists())
        .andExpect(jsonPath("httpStatus").value(httpStatus.name()))
        .andExpect(jsonPath("httpStatusCode").value(httpStatus.value()))
        .andExpect(jsonPath("errorMessage").value("Product with id=%d not found".formatted(id)))
        .andExpect(jsonPath("path").value("/products/%d".formatted(id)))
        .andExpect(jsonPath("errorDetails").doesNotExist());
  }
}

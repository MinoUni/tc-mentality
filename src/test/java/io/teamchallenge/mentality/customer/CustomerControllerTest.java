package io.teamchallenge.mentality.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import io.teamchallenge.mentality.customer.dto.CustomerDto;
import io.teamchallenge.mentality.customer.dto.CustomerPatchDto;
import io.teamchallenge.mentality.customer.dto.CustomerUpdateDto;
import io.teamchallenge.mentality.exception.dto.ApiErrorResponse;
import java.time.LocalDateTime;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@Sql(scripts = "insert-customer.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "reset-customer-seq.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class CustomerControllerTest {

  @LocalServerPort private Integer port;

  @Autowired private TestRestTemplate restTemplate;

  @Test
  @Order(1)
  void shouldGetCustomerById() {
    int id = 1;
    var expected =
        new CustomerDto(
            id,
            "john.doe@example.com",
            "John",
            "Doe",
            "123-456-7890",
            "123 Elm Street, Springfield",
            "john.png",
            LocalDateTime.parse("2023-12-01T08:30:00"));

    var resp =
        restTemplate.getForEntity(
            "http://localhost:%d/customers/{id}".formatted(port), CustomerDto.class, id);

    assertEquals(HttpStatus.OK, resp.getStatusCode());
    assertEquals(APPLICATION_JSON, resp.getHeaders().getContentType());
    var body = resp.getBody();
    assertNotNull(body);
    assertEquals(expected.id(), body.id());
    assertEquals(expected.email(), body.email());
    assertEquals(expected.firstName(), body.firstName());
    assertEquals(expected.lastName(), body.lastName());
    assertEquals(expected.address(), body.address());
    assertEquals(expected.createdAt(), body.createdAt());
  }

  @Test
  @Order(2)
  void shouldFullUpdateCustomerById() {
    var customerUpdateDto =
        new CustomerUpdateDto(
            "Doe",
            "John",
            "0987-654-321",
            "321 Helm Street, Texas",
            "doe.png");

    var resp =
        restTemplate.exchange(
            "http://localhost:%d/customers/{id}".formatted(port),
            HttpMethod.PUT,
            new HttpEntity<>(customerUpdateDto),
            CustomerDto.class,
            1);

    assertEquals(OK, resp.getStatusCode());
    assertEquals(APPLICATION_JSON, resp.getHeaders().getContentType());
    var body = resp.getBody();
    assertNotNull(body);
    assertEquals(1, body.id());
    assertEquals("john.doe@example.com", body.email());
    assertEquals(customerUpdateDto.firstName(), body.firstName());
    assertEquals(customerUpdateDto.lastName(), body.lastName());
    assertEquals(customerUpdateDto.phone(), body.phone());
    assertEquals(customerUpdateDto.address(), body.address());
    assertEquals(customerUpdateDto.profilePicture(), body.profilePicture());
    assertNotNull(body.createdAt());
  }

  @Test
  @Order(3)
  void shouldPatchUpdateCustomerById() {
    var customerPatchDto =
        new CustomerPatchDto(null, null, "0987-654-321", null, "doe.png");

    var resp =
        restTemplate.exchange(
            "http://localhost:%d/customers/{id}".formatted(port),
            HttpMethod.PATCH,
            new HttpEntity<>(customerPatchDto),
            CustomerDto.class,
            1);

    assertEquals(OK, resp.getStatusCode());
    assertEquals(APPLICATION_JSON, resp.getHeaders().getContentType());
    var body = resp.getBody();
    assertNotNull(body);
    assertEquals(1, body.id());
    assertEquals("john.doe@example.com", body.email());
    assertEquals("Doe", body.firstName());
    assertEquals("John", body.lastName());
    assertEquals(customerPatchDto.phone(), body.phone());
    assertEquals("321 Helm Street, Texas", body.address());
    assertEquals(customerPatchDto.profilePicture(), body.profilePicture());
    assertNotNull(body.createdAt());
  }

  @Test
  @Order(4)
  void shouldDeleteCustomerById() {
    var resp =
        restTemplate.exchange(
            "http://localhost:%d/customers/{id}".formatted(port),
            HttpMethod.DELETE,
            HttpEntity.EMPTY,
            Void.class,
            1);

    assertEquals(NO_CONTENT, resp.getStatusCode());
    assertNull(resp.getBody());
  }

  @Test
  @Order(5)
  void shouldReturnErrorWhenDeleteCustomerById() {
    var resp =
        restTemplate.exchange(
            "http://localhost:%d/customers/{id}".formatted(port),
            HttpMethod.DELETE,
            HttpEntity.EMPTY,
            ApiErrorResponse.class,
            1);

    assertEquals(NOT_FOUND, resp.getStatusCode());
    assertEquals(APPLICATION_JSON, resp.getHeaders().getContentType());
    var body = resp.getBody();
    assertNotNull(body);
    assertEquals(NOT_FOUND.name(), body.httpStatus());
    assertEquals(NOT_FOUND.value(), body.httpStatusCode());
  }

  @Test
  @Order(6)
  void shouldReturnNotFoundWhenGetCustomerById() {
    var expectedHttpStatus = NOT_FOUND;

    var resp =
        restTemplate.getForEntity(
            "http://localhost:%d/customers/{id}".formatted(port), ApiErrorResponse.class, 1);

    assertEquals(expectedHttpStatus, resp.getStatusCode());
    assertEquals(APPLICATION_JSON, resp.getHeaders().getContentType());
    var body = resp.getBody();
    assertNotNull(body);
    assertEquals(expectedHttpStatus.name(), body.httpStatus());
    assertEquals(expectedHttpStatus.value(), body.httpStatusCode());
  }
}

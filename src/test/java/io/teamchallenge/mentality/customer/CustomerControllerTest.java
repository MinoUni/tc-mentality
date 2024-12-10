package io.teamchallenge.mentality.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import io.teamchallenge.mentality.customer.dto.CustomerDto;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
class CustomerControllerTest {

  @Autowired private TestRestTemplate restTemplate;

  @Test
  @Sql(scripts = "insert-customer.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "delete-customer.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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

    var response = restTemplate.getForEntity("/customers/{id}", CustomerDto.class, id);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    var body = response.getBody();
    assertNotNull(body);
    assertEquals(expected.id(), body.id());
    assertEquals(expected.email(), body.email());
    assertEquals(expected.firstName(), body.firstName());
    assertEquals(expected.lastName(), body.lastName());
    assertEquals(expected.address(), body.address());
    assertEquals(expected.createdAt(), body.createdAt());
  }
}

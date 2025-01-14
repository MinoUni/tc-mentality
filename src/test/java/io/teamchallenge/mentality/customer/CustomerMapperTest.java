package io.teamchallenge.mentality.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.teamchallenge.mentality.customer.dto.CustomerDto;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class CustomerMapperTest {

  private final CustomerMapper mapper = Mappers.getMapper(CustomerMapper.class);

  @Test
  void toCustomerDto_shouldMapCustomerToCustomerDto() {
    Customer customer = getCustomer();

    CustomerDto customerDto = mapper.toCustomerDto(customer);

    assertNotNull(customerDto);
    assertEquals(customer.getId(), customerDto.id());
    assertEquals(customer.getEmail(), customerDto.email());
    assertEquals(customer.getFirstName(), customerDto.firstName());
    assertEquals(customer.getLastName(), customerDto.lastName());
    assertEquals(customer.getAddress(), customerDto.address());
    assertEquals(customer.getPhone(), customerDto.phone());
    assertEquals(customer.getProfilePicture(), customerDto.profilePicture());
    assertEquals(customer.getCreatedAt(), customerDto.createdAt());
  }

  @Test
  void updateWithNull_shouldUpdateCustomer() {
    Customer customer = getCustomer();
    CustomerDto customerDto =
        new CustomerDto(
            1, "Doe", "John", "Doe", null, "321 Helm Street, Texas", null, LocalDateTime.now());

    mapper.updateWithNull(customerDto, customer);

    assertNotNull(customer);
    assertEquals(1, customer.getId());
    assertEquals(customerDto.firstName(), customer.getFirstName());
    assertEquals(customerDto.lastName(), customer.getLastName());
    assertEquals(customerDto.address(), customer.getAddress());
    assertEquals(customerDto.phone(), customer.getPhone());
    assertEquals(customerDto.profilePicture(), customer.getProfilePicture());
  }

  private Customer getCustomer() {
    return Customer.builder()
        .id(1)
        .email("john.doe@example.com")
        .firstName("John")
        .lastName("Doe")
        .address("123 Elm Street, Springfield")
        .phone("123-456-7890")
        .profilePicture("john.png")
        .createdAt(LocalDateTime.now())
        .build();
  }
}

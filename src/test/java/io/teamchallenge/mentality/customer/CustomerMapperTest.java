package io.teamchallenge.mentality.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import io.teamchallenge.mentality.customer.dto.CustomerPatchDto;
import io.teamchallenge.mentality.customer.dto.CustomerUpdateDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class CustomerMapperTest {

  private final CustomerMapper mapper = Mappers.getMapper(CustomerMapper.class);

  @Test
  void shouldMapCustomerToCustomerDto() {
    var customer = getCustomer();

    var customerDto = mapper.toCustomerDto(customer);

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
  void shouldMapCustomerUpdateDtoIntoCustomer() {
    var customer = getCustomer();
    var customerUpdateDto =
        new CustomerUpdateDto(
            "Doe",
            "John",
            "0987-654-321",
            "321 Helm Street, Texas",
            "doe.png");

    mapper.updateCustomer(customerUpdateDto, customer);

    assertNotNull(customer);
    assertEquals(1, customer.getId());
    assertEquals(customerUpdateDto.firstName(), customer.getFirstName());
    assertEquals(customerUpdateDto.lastName(), customer.getLastName());
    assertEquals(customerUpdateDto.address(), customer.getAddress());
    assertEquals(customerUpdateDto.phone(), customer.getPhone());
    assertEquals(customerUpdateDto.profilePicture(), customer.getProfilePicture());
  }

  @Test
  void shouldMapCustomerPatchDtoIntoCustomer() {
    var customer = getCustomer();
    var customerPatchDto =
        new CustomerPatchDto(
            null,
            null,
            "0987-654-321",
            null,
            "doe.png");

    mapper.patchUpdateCustomer(customerPatchDto, customer);

    assertNotNull(customer);
    assertEquals(1, customer.getId());
    assertNotEquals(customerPatchDto.firstName(), customer.getFirstName());
    assertNotEquals(customerPatchDto.lastName(), customer.getLastName());
    assertEquals(customerPatchDto.phone(), customer.getPhone());
    assertNotEquals(customerPatchDto.address(), customer.getAddress());
    assertEquals(customerPatchDto.profilePicture(), customer.getProfilePicture());
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

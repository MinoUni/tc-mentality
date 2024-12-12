package io.teamchallenge.mentality.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.teamchallenge.mentality.customer.dto.CustomerDto;
import java.time.LocalDateTime;
import java.util.Optional;

import io.teamchallenge.mentality.exception.CustomerNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

  @Mock private CustomerRepository repositoryMock;

  @Mock private CustomerMapper mapperMock;

  @InjectMocks private CustomerService service;

  @Test
  void shouldGetCustomerById() {
    var customer = getCustomer();
    var customerDto = getCustomerDto();

    when(repositoryMock.findById(customer.getId())).thenReturn(Optional.of(customer));
    when(mapperMock.toCustomerDto(customer)).thenReturn(customerDto);

    assertEquals(customerDto, service.getCustomerById(customer.getId()));

    verify(repositoryMock).findById(customer.getId());
    verify(mapperMock).toCustomerDto(customer);
  }

  @Test
  void shouldThrowCustomerNotFoundExceptionWhenGetCustomerById() {
    var customer = getCustomer();
    Integer id = customer.getId();

    when(repositoryMock.findById(customer.getId())).thenThrow(CustomerNotFoundException.class);

    assertThrows(CustomerNotFoundException.class, () -> service.getCustomerById(id));

    verify(repositoryMock).findById(customer.getId());
    verify(mapperMock, never()).toCustomerDto(customer);
  }

  private CustomerDto getCustomerDto() {
    return new CustomerDto(
        1,
        "john.doe@example.com",
        "John",
        "Doe",
        "123 Elm Street, Springfield",
        "123-456-7890",
        "john.png",
        LocalDateTime.now());
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

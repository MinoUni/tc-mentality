package io.teamchallenge.mentality.customer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.teamchallenge.mentality.customer.dto.CustomerDto;
import io.teamchallenge.mentality.exception.CustomerNotFoundException;
import io.teamchallenge.mentality.exception.JsonException;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

  private static final Integer ID = 1;

  private static final String CUSTOMER_NOT_FOUND_MESSAGE = "Customer with id=%d not found";

  @Mock private CustomerRepository customerRepository;

  @Mock private CustomerMapper customerMapper;

  @Mock private ObjectMapper objectMapper;

  @InjectMocks private CustomerService customerService;

  @Test
  void getById_shouldFindCustomerById() {
    final Customer customer = mock(Customer.class);
    final CustomerDto customerDto = mock(CustomerDto.class);

    when(customerRepository.findById(ID)).thenReturn(Optional.of(customer));
    when(customerMapper.toCustomerDto(customer)).thenReturn(customerDto);

    assertDoesNotThrow(() -> customerService.getById(ID));

    verify(customerRepository).findById(ID);
    verify(customerMapper).toCustomerDto(customer);
  }

  @Test
  void getById_shouldThrowNotFoundException() {
    final Customer customer = mock(Customer.class);

    when(customerRepository.findById(ID)).thenThrow(new CustomerNotFoundException(ID));

    var e = assertThrows(CustomerNotFoundException.class, () -> customerService.getById(ID));
    assertEquals(CUSTOMER_NOT_FOUND_MESSAGE.formatted(ID), e.getMessage());

    verify(customerRepository).findById(ID);
    verify(customerMapper, never()).toCustomerDto(customer);
  }

  @Test
  void deleteById_shouldDeleteCustomer() {
    final Customer customer = mock(Customer.class);

    when(customerRepository.existsById(ID)).thenReturn(true);
    when(customerRepository.getReferenceById(ID)).thenReturn(customer);
    doNothing().when(customerRepository).delete(customer);

    assertDoesNotThrow(() -> customerService.deleteById(ID));

    verify(customerRepository).existsById(ID);
    verify(customerRepository).getReferenceById(ID);
    verify(customerRepository).delete(customer);
  }

  @Test
  void deleteById_shouldThrowNotFoundException() {
    when(customerRepository.existsById(ID)).thenReturn(false);

    var e = assertThrows(CustomerNotFoundException.class, () -> customerService.deleteById(ID));
    assertEquals(CUSTOMER_NOT_FOUND_MESSAGE.formatted(ID), e.getMessage());

    verify(customerRepository).existsById(ID);
    verify(customerRepository, never()).getReferenceById(ID);
    verify(customerRepository, never()).delete(any(Customer.class));
  }

  @Test
  void updatePut_shouldFullyUpdateCustomer() {
    final Customer customer = mock(Customer.class);
    final CustomerDto customerDto = mock(CustomerDto.class);

    when(customerRepository.findById(ID)).thenReturn(Optional.of(customer));
    doNothing().when(customerMapper).updateWithNull(customerDto, customer);
    when(customerRepository.save(customer)).thenReturn(customer);
    when(customerMapper.toCustomerDto(customer)).thenReturn(customerDto);

    var actual = assertDoesNotThrow(() -> customerService.updatePut(ID, customerDto));
    assertEquals(customerDto, actual);

    verify(customerRepository).findById(ID);
    verify(customerMapper).updateWithNull(customerDto, customer);
    verify(customerRepository).save(customer);
    verify(customerMapper).toCustomerDto(customer);
  }

  @Test
  void updatePut_shouldThrowNotFoundException() {
    final CustomerDto customerDto = mock(CustomerDto.class);

    when(customerRepository.findById(ID)).thenThrow(new CustomerNotFoundException(ID));

    var e =
        assertThrows(
            CustomerNotFoundException.class, () -> customerService.updatePut(ID, customerDto));
    assertEquals(CUSTOMER_NOT_FOUND_MESSAGE.formatted(ID), e.getMessage());

    verify(customerRepository).findById(ID);
    verify(customerMapper, never()).updateWithNull(eq(customerDto), any(Customer.class));
    verify(customerRepository, never()).save(any(Customer.class));
    verify(customerMapper, never()).toCustomerDto(any(Customer.class));
  }

  @Test
  void updatePatch_shouldUpdatePartiallyCustomer() throws IOException {
    final Customer customer = mock(Customer.class);
    final CustomerDto customerDto = mock(CustomerDto.class);
    final ObjectReader objectReader = mock(ObjectReader.class);
    final JsonNode patchNode = mock(JsonNode.class);

    when(customerRepository.findById(ID)).thenReturn(Optional.of(customer));
    when(customerMapper.toCustomerDto(customer)).thenReturn(customerDto);
    when(objectMapper.readerForUpdating(customerDto)).thenReturn(objectReader);
    when(objectReader.readValue(patchNode)).thenReturn(customerDto);
    doNothing().when(customerMapper).updateWithNull(customerDto, customer);
    when(customerRepository.save(customer)).thenReturn(customer);

    assertDoesNotThrow(() -> customerService.updatePatch(ID, patchNode));

    verify(customerRepository).findById(ID);
    verify(customerMapper, times(2)).toCustomerDto(customer);
    verify(objectMapper).readerForUpdating(customerDto);
    verify(objectReader).readValue(patchNode);
    verify(customerMapper).updateWithNull(customerDto, customer);
    verify(customerRepository).save(customer);
  }

  @Test
  void updatePatch_shouldThrowNotFoundException() {
    final JsonNode customerDto = mock(JsonNode.class);

    when(customerRepository.findById(ID)).thenThrow(new CustomerNotFoundException(ID));

    var e =
        assertThrows(
            CustomerNotFoundException.class, () -> customerService.updatePatch(ID, customerDto));
    assertEquals(CUSTOMER_NOT_FOUND_MESSAGE.formatted(ID), e.getMessage());

    verify(customerRepository).findById(ID);
    verify(customerMapper, never()).toCustomerDto(any(Customer.class));
    verify(objectMapper, never()).readerForUpdating(any(CustomerDto.class));
    verify(customerMapper, never()).updateWithNull(any(CustomerDto.class), any(Customer.class));
    verify(customerRepository, never()).save(any(Customer.class));
  }

  @Test
  void updatePatch_shouldFailToReadJsonNodeAndThrowIOException() throws IOException {
    final String errorMessage =
        "Failed to convert JSON node to `%s` with id=`%d`"
            .formatted(CustomerDto.class.getSimpleName(), ID);
    final Customer customer = mock(Customer.class);
    final CustomerDto customerDto = mock(CustomerDto.class);
    final ObjectReader objectReader = mock(ObjectReader.class);
    final JsonNode patchNode = mock(JsonNode.class);

    when(customerRepository.findById(ID)).thenReturn(Optional.of(customer));
    when(customerMapper.toCustomerDto(customer)).thenReturn(customerDto);
    when(objectMapper.readerForUpdating(customerDto)).thenReturn(objectReader);
    when(objectReader.readValue(patchNode)).thenThrow(IOException.class);

    var e = assertThrows(JsonException.class, () -> customerService.updatePatch(ID, patchNode));
    assertEquals(errorMessage, e.getMessage());

    verify(customerRepository).findById(ID);
    verify(customerMapper, times(1)).toCustomerDto(customer);
    verify(objectMapper).readerForUpdating(customerDto);
    verify(objectReader).readValue(patchNode);
    verify(customerMapper, never()).updateWithNull(customerDto, customer);
    verify(customerRepository, never()).save(customer);
  }
}

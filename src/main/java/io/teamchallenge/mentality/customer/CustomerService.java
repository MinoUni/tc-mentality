package io.teamchallenge.mentality.customer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.teamchallenge.mentality.customer.dto.CustomerDto;
import io.teamchallenge.mentality.exception.CustomerNotFoundException;
import io.teamchallenge.mentality.exception.JsonException;
import io.teamchallenge.mentality.utils.CustomerConstant;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;
  private final ObjectMapper objectMapper;

  public CustomerDto getById(Integer id) {
    return customerMapper.toCustomerDto(getCustomer(id));
  }

  @Transactional
  public void deleteById(Integer id) {
    if (!customerRepository.existsById(id)) {
      log.info(CustomerConstant.CUSTOMER_WITH_ID_NOT_FOUND, id);
      throw new CustomerNotFoundException(id);
    }
    var customer = customerRepository.getReferenceById(id);
    customerRepository.delete(customer);
    log.info(CustomerConstant.CUSTOMER_WITH_ID_DELETED, id);
  }

  @Transactional
  public CustomerDto updatePut(Integer id, CustomerDto customerUpdateDto) {
    var customer = getCustomer(id);
    customerMapper.updateWithNull(customerUpdateDto, customer);
    customer = customerRepository.save(customer);
    return customerMapper.toCustomerDto(customer);
  }

  @Transactional
  public CustomerDto updatePatch(Integer id, JsonNode patchNode) {
    var customer = getCustomer(id);
    var customerDto = customerMapper.toCustomerDto(customer);
    try {
      objectMapper.readerForUpdating(customerDto).readValue(patchNode);
    } catch (IOException e) {
      log.warn("Failed to convert JSON node to `{}` with id=`{}`", CustomerDto.class, id, e);
      throw new JsonException(CustomerDto.class, id);
    }
    customerMapper.updateWithNull(customerDto, customer);
    customer = customerRepository.save(customer);
    return customerMapper.toCustomerDto(customer);
  }

  private Customer getCustomer(final Integer id) {
    return customerRepository
        .findById(id)
        .orElseThrow(
            () -> {
              log.info(CustomerConstant.CUSTOMER_WITH_ID_NOT_FOUND, id);
              return new CustomerNotFoundException(id);
            });
  }
}

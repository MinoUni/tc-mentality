package io.teamchallenge.mentality.customer;

import io.teamchallenge.mentality.customer.dto.CustomerDto;
import io.teamchallenge.mentality.customer.dto.CustomerPatchDto;
import io.teamchallenge.mentality.customer.dto.CustomerUpdateDto;
import io.teamchallenge.mentality.exception.CustomerNotFoundException;
import io.teamchallenge.mentality.utils.CustomerConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

  private final CustomerRepository repository;
  private final CustomerMapper mapper;

  public CustomerDto getById(Integer id) {
    return mapper.toCustomerDto(getCustomer(id));
  }

  @Transactional
  public void deleteCustomerById(Integer id) {
    if (!repository.existsById(id)) {
      log.info(CustomerConstant.CUSTOMER_WITH_ID_NOT_FOUND, id);
      throw new CustomerNotFoundException(id);
    }
    var customer = repository.getReferenceById(id);
    repository.delete(customer);
  }

  @Transactional
  public CustomerDto updateCustomer(Integer id, CustomerUpdateDto customerUpdateDto) {
    var customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    mapper.updateCustomer(customerUpdateDto, customer);
    repository.save(customer);
    return mapper.toCustomerDto(customer);
  }

  @Transactional
  public CustomerDto patchUpdateCustomer(Integer id, CustomerPatchDto customerPatchDto) {
    var customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    mapper.patchUpdateCustomer(customerPatchDto, customer);
    repository.save(customer);
    return mapper.toCustomerDto(customer);
  }

  private Customer getCustomer(final Integer id) {
    return repository
        .findById(id)
        .orElseThrow(
            () -> {
              log.info(CustomerConstant.CUSTOMER_WITH_ID_NOT_FOUND, id);
              return new CustomerNotFoundException(id);
            });
  }
}

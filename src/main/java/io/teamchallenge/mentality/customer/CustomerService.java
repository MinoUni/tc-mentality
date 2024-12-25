package io.teamchallenge.mentality.customer;

import io.teamchallenge.mentality.customer.dto.CustomerDto;
import io.teamchallenge.mentality.customer.dto.CustomerPatchDto;
import io.teamchallenge.mentality.customer.dto.CustomerUpdateDto;
import io.teamchallenge.mentality.exception.CustomerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class CustomerService {

  private final CustomerRepository repository;
  private final CustomerMapper mapper;

  public CustomerService(
      CustomerRepository repository, @Qualifier("customerMapperImpl") CustomerMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public CustomerDto getCustomerById(Integer id) {
    var customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    return mapper.toCustomerDto(customer);
  }

  @Transactional
  public void deleteCustomerById(Integer id) {
    if (!repository.existsById(id)) {
      log.info("Customer with id={} not found", id);
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
}

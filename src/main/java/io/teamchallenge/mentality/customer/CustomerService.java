package io.teamchallenge.mentality.customer;

import io.teamchallenge.mentality.customer.dto.CustomerDto;
import io.teamchallenge.mentality.exception.CustomerNotFoundException;
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

  public CustomerDto getCustomerById(Integer id) {
    Customer customer =
        customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    return customerMapper.toCustomerDto(customer);
  }

  @Transactional
  public void deleteCustomerById(Integer id) {
    if (!customerRepository.existsById(id)) {
      log.info("Customer with id={} not found", id);
      throw new CustomerNotFoundException(id);
    }
    var customer = customerRepository.getReferenceById(id);
    customerRepository.delete(customer);
  }
}

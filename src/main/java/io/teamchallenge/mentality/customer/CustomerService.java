package io.teamchallenge.mentality.customer;

import io.teamchallenge.mentality.customer.dto.CustomerDto;
import io.teamchallenge.mentality.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;

  public CustomerDto getCustomerById(Integer id) {
    Customer customer =
        customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    return customerMapper.toCustomerDto(customer);
  }
}

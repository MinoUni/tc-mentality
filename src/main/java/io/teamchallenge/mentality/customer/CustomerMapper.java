package io.teamchallenge.mentality.customer;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import io.teamchallenge.mentality.customer.dto.CustomerDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = SPRING)
public interface CustomerMapper {

  CustomerDto toCustomerDto(Customer customer);
}

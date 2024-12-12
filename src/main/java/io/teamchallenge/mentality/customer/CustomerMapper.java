package io.teamchallenge.mentality.customer;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import io.teamchallenge.mentality.customer.dto.CustomerDto;
import io.teamchallenge.mentality.customer.dto.CustomerPatchDto;
import io.teamchallenge.mentality.customer.dto.CustomerUpdateDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

  CustomerDto toCustomerDto(Customer customer);

  void updateCustomer(CustomerUpdateDto customerUpdateDto, @MappingTarget Customer customer);

  void patchUpdateCustomer(CustomerPatchDto customerPatchDto, @MappingTarget Customer customer);
}

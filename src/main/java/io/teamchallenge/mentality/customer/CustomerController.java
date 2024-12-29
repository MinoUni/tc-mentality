package io.teamchallenge.mentality.customer;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.teamchallenge.mentality.customer.dto.CustomerDto;
import io.teamchallenge.mentality.customer.dto.CustomerPatchDto;
import io.teamchallenge.mentality.customer.dto.CustomerUpdateDto;
import io.teamchallenge.mentality.exception.dto.ApiErrorResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
@Tag(name = "Customers endpoints", description = "Endpoints for customer data manipulation")
class CustomerController {

  private final CustomerService customerService;

  @Operation(
      summary = "Find customer data by id",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Customer data found",
            content = {
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = CustomerDto.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Customer data not found",
            content = {
              @Content(
                  mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                  schema = @Schema(implementation = ApiErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = {
              @Content(
                  mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                  schema = @Schema(implementation = ApiErrorResponse.class))
            })
      })
  @GetMapping("/{id}")
  public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Integer id) {
    return ResponseEntity.ok(customerService.getCustomerById(id));
  }

  @Operation(
      summary = "Delete customer data by id",
      responses = {
        @ApiResponse(
            responseCode = "204",
            description = "Customer deleted",
            content = {@Content()}),
        @ApiResponse(
            responseCode = "404",
            description = "Customer data not found",
            content = {
              @Content(
                  mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                  schema = @Schema(implementation = ApiErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = {
              @Content(
                  mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                  schema = @Schema(implementation = ApiErrorResponse.class))
            })
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteCustomerById(@PathVariable Integer id) {
    customerService.deleteCustomerById(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Update customer data",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Customer data updated",
            content = {
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = CustomerDto.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Customer data not found",
            content = {
              @Content(
                  mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                  schema = @Schema(implementation = ApiErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = {
              @Content(
                  mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                  schema = @Schema(implementation = ApiErrorResponse.class))
            })
      })
  @PutMapping("/{id}")
  public ResponseEntity<CustomerDto> updateCustomerById(
      @PathVariable Integer id, @Valid @RequestBody CustomerUpdateDto customer) {
    return ResponseEntity.ok(customerService.updateCustomer(id, customer));
  }

  @Operation(
      summary = "Partially update customer data",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Customer data partially updated",
            content = {
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = CustomerDto.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Customer data not found",
            content = {
              @Content(
                  mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                  schema = @Schema(implementation = ApiErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = {
              @Content(
                  mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                  schema = @Schema(implementation = ApiErrorResponse.class))
            })
      })
  @PatchMapping("/{id}")
  public ResponseEntity<CustomerDto> patch(
      @PathVariable Integer id, @RequestBody CustomerPatchDto customer) {
    return ResponseEntity.ok(customerService.patchUpdateCustomer(id, customer));
  }
}

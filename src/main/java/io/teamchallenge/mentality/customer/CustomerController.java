package io.teamchallenge.mentality.customer;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.teamchallenge.mentality.customer.dto.CustomerDto;
import io.teamchallenge.mentality.exception.dto.ApiErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ApiErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "500",
            description = "Server error",
            content = {
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ApiErrorResponse.class))
            })
      })
  @GetMapping("/{id}")
  public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Integer id) {
    return ResponseEntity.ok(customerService.getCustomerById(id));
  }
}

package io.teamchallenge.mentality.customer;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.teamchallenge.mentality.customer.dto.CustomerDto;
import io.teamchallenge.mentality.exception.CustomerNotFoundException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

  private static final Integer ID = 1;

  @Autowired private MockMvc mockMvc;

  @MockitoBean private CustomerService customerService;

  @Test
  void getOneById_shouldReturnCustomerDto() throws Exception {
    final CustomerDto customerDto =
        new CustomerDto(
            1,
            "john.doe@gmail.com",
            "John",
            "Doe",
            "380445556677",
            "Mtis City, Sn Pat street 88",
            "jdavatar.png",
            LocalDateTime.now());
    when(customerService.getById(ID)).thenReturn(customerDto);

    mockMvc
        .perform(get("/customers/{id}", ID))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(customerDto.id()))
        .andExpect(jsonPath("$.email").value(customerDto.email()))
        .andExpect(jsonPath("$.firstName").value(customerDto.firstName()))
        .andExpect(jsonPath("$.lastName").value(customerDto.lastName()))
        .andExpect(jsonPath("$.phone").value(customerDto.phone()))
        .andExpect(jsonPath("$.address").value(customerDto.address()))
        .andExpect(jsonPath("$.profilePicture").value(customerDto.profilePicture()))
        .andExpect(jsonPath("$.createdAt").exists());

    verify(customerService).getById(ID);
  }

  @Test
  void getOneById_shouldNotFound() throws Exception {
    final HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    when(customerService.getById(ID)).thenThrow(new CustomerNotFoundException(ID));

    mockMvc
        .perform(get("/customers/{id}", ID))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.httpStatus").value(httpStatus.name()))
        .andExpect(jsonPath("$.httpStatusCode").value(httpStatus.value()))
        .andExpect(jsonPath("$.errorMessage").value("Customer with id=%d not found.".formatted(ID)))
        .andExpect(jsonPath("$.path").value("/customers/%d".formatted(ID)))
        .andExpect(jsonPath("$.errorDetails").doesNotExist());

    verify(customerService).getById(ID);
  }

  @Test
  void delete_shouldReturnNoContent() throws Exception {
    doNothing().when(customerService).deleteById(ID);

    mockMvc.perform(delete("/customers/{id}", ID)).andExpect(status().isNoContent());

    verify(customerService).deleteById(ID);
  }

  @Test
  void delete_shouldNotFound() throws Exception {
    final HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    doThrow(new CustomerNotFoundException(ID)).when(customerService).deleteById(ID);

    mockMvc
        .perform(delete("/customers/{id}", ID))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.httpStatus").value(httpStatus.name()))
        .andExpect(jsonPath("$.httpStatusCode").value(httpStatus.value()))
        .andExpect(jsonPath("$.errorMessage").value("Customer with id=%d not found.".formatted(ID)))
        .andExpect(jsonPath("$.path").value("/customers/%d".formatted(ID)))
        .andExpect(jsonPath("$.errorDetails").doesNotExist());

    verify(customerService).deleteById(ID);
  }
}

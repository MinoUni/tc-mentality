package io.teamchallenge.mentality.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.teamchallenge.mentality.product.dto.ProductMinimalDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private ProductService mockedProductService;

  @Test
  void getAll_shouldReturnPageOfProducts() throws Exception {
    var products = new PagedModel<ProductMinimalDto>(Page.empty());
    when(mockedProductService.getAll(any(), any())).thenReturn(products);

    mockMvc
        .perform(
            get("/products")
                .param("nameContain", "car")
                .param("category", "carpets")
                .param("minPrice", String.valueOf(10))
                .param("maxPrice", String.valueOf(10)))
        .andExpectAll(status().isOk(), content().contentType(MediaType.APPLICATION_JSON));

    verify(mockedProductService).getAll(any(), any());
  }
}

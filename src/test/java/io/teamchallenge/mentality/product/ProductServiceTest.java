package io.teamchallenge.mentality.product;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.teamchallenge.mentality.exception.JsonException;
import io.teamchallenge.mentality.exception.ProductNotFoundException;
import io.teamchallenge.mentality.product.category.Category;
import io.teamchallenge.mentality.product.category.CategoryRepository;
import io.teamchallenge.mentality.product.category.ProductCategory;
import io.teamchallenge.mentality.product.dto.ProductDto;
import io.teamchallenge.mentality.product.dto.ProductFilter;
import io.teamchallenge.mentality.product.dto.ProductMinimalDto;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  private static final Integer ID = 1;

  private static final String PRODUCT_NOT_FOUND_MESSAGE = "Product with id=%d not found";

  @Mock private ProductRepository productRepository;

  @Mock private CategoryRepository categoryRepository;

  @Mock private ProductMapper productMapper;

  @Mock private ObjectMapper objectMapper;

  @InjectMocks private ProductService productService;

  @Test
  void getById_shouldFindProductByIdAndMapToProductDto() {
    final Product product = mock(Product.class);
    final ProductDto productDto = mock(ProductDto.class);
    when(productRepository.findById(ID)).thenReturn(Optional.of(product));
    when(productMapper.toProductDto(product)).thenReturn(productDto);

    assertDoesNotThrow(() -> productService.getById(ID));

    verify(productRepository).findById(ID);
    verify(productMapper).toProductDto(product);
  }

  @Test
  void getById_shouldThrowProductNotFoundException() {
    final String errorMessage = PRODUCT_NOT_FOUND_MESSAGE.formatted(ID);
    when(productRepository.findById(ID)).thenThrow(new ProductNotFoundException(ID));

    var e = assertThrows(ProductNotFoundException.class, () -> productService.getById(ID));

    assertEquals(e.getMessage(), errorMessage);
    verify(productRepository).findById(ID);
    verify(productMapper, never()).toProductDto(any(Product.class));
  }

  @Test
  void getAll_shouldFindProductsAndMapToPage() {
    final ProductFilter filter = mock(ProductFilter.class);
    final Pageable pageable = mock(PageRequest.class);
    final Product product = mock(Product.class);
    final ProductMinimalDto productDto = mock(ProductMinimalDto.class);
    final Specification<Product> specification = Specification.where(null);
    final Page<Product> products = new PageImpl<>(List.of(product));

    when(filter.toSpecification()).thenReturn(specification);
    when(productRepository.findAll(specification, pageable)).thenReturn(products);
    when(productMapper.toProductMinimalDto(product)).thenReturn(productDto);

    assertDoesNotThrow(() -> productService.getAll(filter, pageable));

    verify(filter).toSpecification();
    verify(productRepository).findAll(specification, pageable);
    verify(productMapper).toProductMinimalDto(product);
  }

  @Test
  void deleteById_shouldDeleteProduct() {
    final Product product = mock(Product.class);
    when(productRepository.existsById(ID)).thenReturn(true);
    when(productRepository.getReferenceById(ID)).thenReturn(product);
    doNothing().when(productRepository).delete(product);

    assertDoesNotThrow(() -> productService.deleteById(ID));

    verify(productRepository).existsById(ID);
    verify(productRepository).getReferenceById(ID);
    verify(productRepository).delete(product);
  }

  @Test
  void deleteById_shouldThrowProductNotFoundException() {
    String errorMessage = PRODUCT_NOT_FOUND_MESSAGE.formatted(ID);
    when(productRepository.existsById(ID)).thenReturn(false);

    var e = assertThrows(ProductNotFoundException.class, () -> productService.deleteById(ID));

    assertEquals(errorMessage, e.getMessage());
    verify(productRepository).existsById(ID);
    verify(productRepository, never()).getReferenceById(ID);
    verify(productRepository, never()).delete(any(Product.class));
  }

  @Test
  void putUpdate_shouldPutUpdateAndSaveProduct() {
    final ProductDto productDto = mock(ProductDto.class);
    final Product product = mock(Product.class);
    final Category category = mock(Category.class);

    when(productRepository.findById(ID)).thenReturn(Optional.of(product));
    doNothing().when(productMapper).updateWithNull(productDto, product);
    when(productDto.category()).thenReturn(ProductCategory.CARPETS.name());
    when(categoryRepository.getReferenceById(ID)).thenReturn(category);
    when(productRepository.save(product)).thenReturn(product);
    when(productMapper.toProductDto(product)).thenReturn(productDto);

    ProductDto actual = assertDoesNotThrow(() -> productService.putUpdate(ID, productDto));

    assertEquals(productDto, actual);
    verify(productRepository).findById(ID);
    verify(productMapper).updateWithNull(productDto, product);
    verify(categoryRepository).getReferenceById(ID);
    verify(productRepository).save(product);
    verify(productMapper).toProductDto(product);
  }

  @Test
  void putUpdate_shouldThrowProductNotFoundException() {
    final ProductDto productDto = mock(ProductDto.class);
    String errorMessage = PRODUCT_NOT_FOUND_MESSAGE.formatted(ID);
    when(productRepository.findById(ID)).thenThrow(new ProductNotFoundException(ID));

    var e =
        assertThrows(
            ProductNotFoundException.class, () -> productService.putUpdate(ID, productDto));

    assertEquals(errorMessage, e.getMessage());
    verify(productRepository).findById(ID);
    verify(productMapper, never()).updateWithNull(eq(productDto), any(Product.class));
    verify(categoryRepository, never()).getReferenceById(ID);
    verify(productRepository, never()).save(any(Product.class));
    verify(productMapper, never()).toProductDto(any(Product.class));
  }

  @Test
  void patchUpdate_shouldPartiallyUpdateAndSaveProduct() throws IOException {
    final JsonNode jsonNode = mock(JsonNode.class);
    final Product product = mock(Product.class);
    final ProductDto productDto = mock(ProductDto.class);
    final ObjectReader objectReader = mock(ObjectReader.class);
    when(productRepository.findById(ID)).thenReturn(Optional.of(product));
    when(productMapper.toProductDto(product)).thenReturn(productDto);
    when(objectMapper.readerForUpdating(productDto)).thenReturn(objectReader);
    when(objectReader.readValue(jsonNode)).thenReturn(productDto);
    doNothing().when(productMapper).updateWithNull(productDto, product);
    when(productRepository.save(product)).thenReturn(product);

    assertDoesNotThrow(() -> productService.patchUpdate(ID, jsonNode));

    verify(productRepository).findById(ID);
    verify(productMapper, times(2)).toProductDto(product);
    verify(objectMapper).readerForUpdating(productDto);
    verify(objectReader).readValue(jsonNode);
    verify(productMapper).updateWithNull(productDto, product);
    verify(productRepository).save(product);
  }

  @Test
  void patchUpdate_shouldThrowProductNotFoundException() {
    final JsonNode jsonNode = mock(JsonNode.class);
    String errorMessage = PRODUCT_NOT_FOUND_MESSAGE.formatted(ID);
    when(productRepository.findById(ID)).thenThrow(new ProductNotFoundException(ID));

    var e =
        assertThrows(
            ProductNotFoundException.class, () -> productService.patchUpdate(ID, jsonNode));

    assertEquals(errorMessage, e.getMessage());
    verify(productRepository).findById(ID);
    verify(productMapper, never()).toProductDto(any(Product.class));
    verify(objectMapper, never()).readerForUpdating(any(ProductDto.class));
    verify(productMapper, never()).updateWithNull(any(ProductDto.class), any(Product.class));
    verify(productRepository, never()).save(any(Product.class));
  }

  @Test
  void patchUpdate_shouldThrowIOExceptionWhenReadingJsonNode() throws IOException {
    final String errorMessage =
        "Failed to convert JSON node to `%s` with id=`%d`"
            .formatted(ProductDto.class.getSimpleName(), ID);
    final JsonNode jsonNode = mock(JsonNode.class);
    final Product product = mock(Product.class);
    final ProductDto productDto = mock(ProductDto.class);
    final ObjectReader objectReader = mock(ObjectReader.class);
    when(productRepository.findById(ID)).thenReturn(Optional.of(product));
    when(productMapper.toProductDto(product)).thenReturn(productDto);
    when(objectMapper.readerForUpdating(productDto)).thenReturn(objectReader);
    when(objectReader.readValue(jsonNode)).thenThrow(IOException.class);

    var e = assertThrows(JsonException.class, () -> productService.patchUpdate(ID, jsonNode));

    assertEquals(errorMessage, e.getMessage());
    verify(productRepository).findById(ID);
    verify(productMapper, times(1)).toProductDto(product);
    verify(objectMapper).readerForUpdating(productDto);
    verify(objectReader).readValue(jsonNode);
    verify(productMapper, never()).updateWithNull(productDto, product);
    verify(productRepository, never()).save(product);
  }
}

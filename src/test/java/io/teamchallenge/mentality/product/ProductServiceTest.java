package io.teamchallenge.mentality.product;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.teamchallenge.mentality.exception.ProductNotFoundException;
import io.teamchallenge.mentality.product.category.Category;
import io.teamchallenge.mentality.product.category.CategoryRepository;
import io.teamchallenge.mentality.product.category.ProductCategory;
import io.teamchallenge.mentality.product.dto.ProductDto;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  private static final Integer ID = 1;

  @Mock private ProductRepository productRepository;

  @Mock private CategoryRepository categoryRepository;

  @Mock private ProductMapper productMapper;

  @InjectMocks private ProductService productService;

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
    String errorMessage = "Product with id=%d not found".formatted(ID);
    when(productRepository.existsById(ID)).thenReturn(false);

    var e = assertThrows(ProductNotFoundException.class, () -> productService.deleteById(ID));

    assertEquals(errorMessage, e.getMessage());
    verify(productRepository).existsById(ID);
    verify(productRepository, never()).getReferenceById(ID);
    verify(productRepository, never()).delete(any(Product.class));
  }

  @Test
  void update_shouldUpdateAndSaveProduct() {
    final ProductDto productDto = mock(ProductDto.class);
    final Product product = mock(Product.class);
    final Category category = mock(Category.class);

    when(productRepository.findById(ID)).thenReturn(Optional.of(product));
    doNothing().when(productMapper).updateWithNull(productDto, product);
    when(productDto.category()).thenReturn(ProductCategory.CARPETS.name());
    when(categoryRepository.getReferenceById(ID)).thenReturn(category);
    when(productRepository.save(product)).thenReturn(product);
    when(productMapper.toProductDto(product)).thenReturn(productDto);

    ProductDto actual = assertDoesNotThrow(() -> productService.update(ID, productDto));

    assertEquals(productDto, actual);
    verify(productRepository).findById(ID);
    verify(productMapper).updateWithNull(productDto, product);
    verify(categoryRepository).getReferenceById(ID);
    verify(productRepository).save(product);
    verify(productMapper).toProductDto(product);
  }

  @Test
  void update_shouldThrowProductNotFoundException() {
    final ProductDto productDto = mock(ProductDto.class);
    String errorMessage = "Product with id=%d not found".formatted(ID);
    when(productRepository.findById(ID)).thenThrow(new ProductNotFoundException(ID));

    var e =
        assertThrows(ProductNotFoundException.class, () -> productService.update(ID, productDto));

    assertEquals(errorMessage, e.getMessage());
    verify(productRepository).findById(ID);
    verify(productMapper, never()).updateWithNull(eq(productDto), any(Product.class));
    verify(categoryRepository, never()).getReferenceById(ID);
    verify(productRepository, never()).save(any(Product.class));
    verify(productMapper, never()).toProductDto(any(Product.class));
  }
}

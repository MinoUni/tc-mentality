package io.teamchallenge.mentality.product;

import io.teamchallenge.mentality.exception.ProductNotFoundException;
import io.teamchallenge.mentality.product.dto.ProductDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ProductMapper productMapper;

  public ProductDto getById(Integer id) {
    return productMapper.toProductDto(
        productRepository
            .findById(id)
            .orElseThrow(
                () -> {
                  log.info("Product with id=`{}` not found!", id);
                  return new ProductNotFoundException(id);
                }));
  }

  @Transactional
  public Integer create(ProductDto productDto) {
    Product product = productMapper.toEntity(productDto);
    product
        .setCategory(categoryRepository.getReferenceById(productDto.category().getId()))
        .setSku(UUID.randomUUID().toString()); // ? replace with SKU generator func
    return productRepository.save(product).getId();
  }
}

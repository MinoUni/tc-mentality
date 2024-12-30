package io.teamchallenge.mentality.product;

import io.teamchallenge.mentality.exception.ProductNotFoundException;
import io.teamchallenge.mentality.product.category.CategoryRepository;
import io.teamchallenge.mentality.product.category.ProductCategory;
import io.teamchallenge.mentality.product.constant.ProductConstant;
import io.teamchallenge.mentality.product.dto.ProductDto;
import io.teamchallenge.mentality.product.dto.ProductFilter;
import io.teamchallenge.mentality.product.dto.ProductMinimalDto;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ProductMapper productMapper;

  public ProductService(
      ProductRepository productRepository,
      CategoryRepository categoryRepository,
      @Qualifier("productMapperImpl") ProductMapper productMapper) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
    this.productMapper = productMapper;
  }

  public ProductDto getById(Integer id) {
    return productMapper.toProductDto(
        productRepository
            .findById(id)
            .orElseThrow(
                () -> {
                  log.info(ProductConstant.PRODUCT_WITH_ID_NOT_FOUND, id);
                  return new ProductNotFoundException(id);
                }));
  }

  @Transactional
  public Integer create(ProductDto productDto) {
    Product product = productMapper.toEntity(productDto);
    ProductCategory category = ProductCategory.valueOf(productDto.category());
    product
        .setCategory(categoryRepository.getReferenceById(category.getId()))
        .setSku(UUID.randomUUID().toString()); // ? replace with SKU generator func
    return productRepository.save(product).getId();
  }

  public PagedModel<ProductMinimalDto> getAll(ProductFilter filter, Pageable pageable) {
    Specification<Product> spec = filter.toSpecification();
    Page<Product> products = productRepository.findAll(spec, pageable);
    Page<ProductMinimalDto> productDto = products.map(productMapper::toProductMinimalDto);
    return new PagedModel<>(productDto);
  }

  @Transactional
  public void deleteById(Integer id) {
    if (!productRepository.existsById(id)) {
      log.info(ProductConstant.PRODUCT_WITH_ID_NOT_FOUND, id);
      throw new ProductNotFoundException(id);
    }
    Product product = productRepository.getReferenceById(id);
    productRepository.delete(product);
    log.info("Product with id=`{}` deleted", id);
  }

  @Transactional
  public ProductDto update(Integer id, ProductDto productDto) {
    Product product =
        productRepository
            .findById(id)
            .orElseThrow(
                () -> {
                  log.info(ProductConstant.PRODUCT_WITH_ID_NOT_FOUND, id);
                  return new ProductNotFoundException(id);
                });
    productMapper.updateWithNull(productDto, product);
    product.setCategory(
        categoryRepository.getReferenceById(
            ProductCategory.valueOf(productDto.category()).getId()));
    Product updatedProduct = productRepository.save(product);
    return productMapper.toProductDto(updatedProduct);
  }
}

package io.teamchallenge.mentality.product;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.teamchallenge.mentality.exception.dto.ApiErrorResponse;
import io.teamchallenge.mentality.product.dto.ProductDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@Tag(name = "Products endpoints", description = "Endpoints for products data manipulation")
class ProductController {

  private final ProductService productService;

  @Operation(
      summary = "Find product data by id",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Product data found",
            content = {
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ProductDto.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Product data not found",
            content = {
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ApiErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = {
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ApiErrorResponse.class))
            })
      })
  @GetMapping("/{id}")
  public ResponseEntity<ProductDto> getOneById(@PathVariable Integer id) {
    return ResponseEntity.ok(productService.getById(id));
  }

  @Operation(
      summary = "Create new product",
      description = "Ignore `id`, `sku` props, they initialized on server side",
      responses = {
        @ApiResponse(
            responseCode = "201",
            description = "Product created",
            headers = {
              @Header(
                  name = "Location",
                  description = "URI to resource",
                  schema = @Schema(type = "string"))
            },
            content = {@Content}),
        @ApiResponse(
            responseCode = "409",
            description = "Product already exists",
            content = {
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ApiErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = {
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ApiErrorResponse.class))
            })
      })
  @PostMapping
  public ResponseEntity<String> create(@RequestBody @Valid ProductDto productDto) {
    Integer productId = productService.create(productDto);
    return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productId)
                .toUri())
        .build();
  }
}
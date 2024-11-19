package io.teamchallenge.mentality.product;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products_images")
public class ProductImage {

  @Id
  private String imageFilename;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_sku", referencedColumnName = "sku")
  private Product product;
}

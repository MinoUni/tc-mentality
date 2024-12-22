package io.teamchallenge.mentality.product;

import io.hypersistence.utils.hibernate.type.money.MonetaryAmountType;
import io.teamchallenge.mentality.customer.CustomerCart;
import io.teamchallenge.mentality.order.OrderItem;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CompositeType;
import org.hibernate.annotations.NaturalId;
import org.javamoney.moneta.Money;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NaturalId
  @Column(nullable = false, unique = true)
  private String sku; // stockKeepingUnit

  @Column(nullable = false)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @CompositeType(MonetaryAmountType.class)
  @AttributeOverride(name = "amount", column = @Column(name = "price_amount", nullable = false))
  @AttributeOverride(name = "currency", column = @Column(name = "price_currency", nullable = false))
  private Money price;

  @Column(nullable = false)
  private Integer quantityInStock;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category", referencedColumnName = "name")
  private Category category;

  @Builder.Default
  @ElementCollection(targetClass = String.class)
  @CollectionTable(
      name = "product_images",
      joinColumns = @JoinColumn(name = "product_sku", referencedColumnName = "sku"))
  @OrderColumn(name = "index_id")
  @Column(name = "image_url", nullable = false)
  private List<String> imagesUrls = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> items = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CustomerCart> customerCarts = new ArrayList<>();

  public BigDecimal getPriceAmount() {
    return this.price.getNumber().numberValue(BigDecimal.class);
  }

  public String getPriceCurrency() {
    return this.price.getCurrency().getCurrencyCode();
  }

  public Product setCategory(Category category) {
    this.category = category;
    this.category.getProducts().add(this);
    return this;
  }
}

package io.teamchallenge.mentality.customer;

import io.hypersistence.utils.hibernate.type.money.MonetaryAmountType;
import io.teamchallenge.mentality.product.Product;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import javax.money.MonetaryAmount;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CompositeType;

@Getter
@Setter
@Entity
@Table(name = "customer_cart")
public class CustomerCart {

  @EmbeddedId
  private CustomerCartId id;

  @Column(nullable = false)
  private Integer quantity;

  @Transient
  @CompositeType(MonetaryAmountType.class)
  @AttributeOverride(name = "amount", column = @Column(name = "price_amount", nullable = false))
  private MonetaryAmount price;

  @MapsId("customerId")
  @OneToOne(fetch = FetchType.LAZY)
  private Customer customer;

  @MapsId("productId")
  @ManyToOne(fetch = FetchType.LAZY)
  private Product product;

  public CustomerCart(Customer customer, Product product, Integer quantity, MonetaryAmount price) {
    this.id = new CustomerCartId(customer.getId(), product.getId());
    this.customer = customer;
    this.product = product;
    this.quantity = quantity;
    this.price = price;
  }
}

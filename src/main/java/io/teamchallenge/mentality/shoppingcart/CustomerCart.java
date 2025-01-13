package io.teamchallenge.mentality.shoppingcart;

import io.hypersistence.utils.hibernate.type.money.MonetaryAmountType;
import io.teamchallenge.mentality.customer.Customer;
import io.teamchallenge.mentality.product.Product;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import javax.money.MonetaryAmount;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CompositeType;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customers_carts")
public class CustomerCart {

  @EmbeddedId
  private CustomerCartId id;

  @Column(nullable = false)
  private Integer quantity;

  @Transient
  @CompositeType(MonetaryAmountType.class)
  @AttributeOverride(name = "amount", column = @Column(name = "price_amount", nullable = false))
  private MonetaryAmount price;

  @MapsId("customerEmail")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_email", referencedColumnName = "email")
  private Customer customer;

  @MapsId("productSku")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_sku", referencedColumnName = "sku")
  private Product product;

  public CustomerCart(Customer customer, Product product, Integer quantity, MonetaryAmount price) {
    this.id = new CustomerCartId(customer.getEmail(), product.getSku());
    this.customer = customer;
    this.product = product;
    this.quantity = quantity;
    this.price = price;
  }
}

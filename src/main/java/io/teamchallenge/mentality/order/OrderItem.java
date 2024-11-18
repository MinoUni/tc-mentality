package io.teamchallenge.mentality.order;

import io.hypersistence.utils.hibernate.type.money.MonetaryAmountType;
import io.teamchallenge.mentality.product.Product;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import javax.money.MonetaryAmount;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CompositeType;

@Setter
@Getter
@Entity
@Table(name = "orders_items")
public class OrderItem {

  @EmbeddedId
  private OrderItemId id;

  @Column(nullable = false)
  private Integer quantity;

  @Transient
  @CompositeType(MonetaryAmountType.class)
  @AttributeOverride(name = "amount", column = @Column(name = "price_amount", nullable = false))
  private MonetaryAmount price;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("productId")
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("orderId")
  private Order order;

  public OrderItem(Product product, Order order, Integer quantity, MonetaryAmount price) {
    this.id = new OrderItemId(product.getId(), order.getId());
    this.product = product;
    this.order = order;
    this.quantity = quantity;
    this.price = price;
  }
}

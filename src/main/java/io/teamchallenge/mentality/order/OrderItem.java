package io.teamchallenge.mentality.order;

import io.teamchallenge.mentality.product.Product;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "order_items")
public class OrderItem {

  @EmbeddedId
  private OrderItemId id;

  @Column(nullable = false)
  private Integer quantity;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("productId")
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("orderId")
  private Order order;

  public OrderItem(Product product, Order order) {
    this.product = product;
    this.order = order;
    this.id = new OrderItemId(product.getId(), order.getId());
  }
}

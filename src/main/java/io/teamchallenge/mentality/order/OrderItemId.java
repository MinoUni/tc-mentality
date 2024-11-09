package io.teamchallenge.mentality.order;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;

@Getter
@Embeddable
public class OrderItemId implements Serializable {

  @Column(name = "product_id")
  private Integer productId;

  @Column(name = "order_id")
  private Integer orderId;

  public OrderItemId(Integer productId, Integer orderId) {
    this.productId = productId;
    this.orderId = orderId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof OrderItemId that)) {
      return false;
    }
    return Objects.equals(productId, that.productId) && Objects.equals(orderId, that.orderId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productId, orderId);
  }
}

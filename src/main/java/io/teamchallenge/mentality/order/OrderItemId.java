package io.teamchallenge.mentality.order;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Embeddable
public class OrderItemId implements Serializable {

  @Column
  private String productSku;

  @Column
  private String orderCode;

  public OrderItemId(String productSku, String orderCode) {
    this.productSku = productSku;
    this.orderCode = orderCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof OrderItemId that)) {
      return false;
    }
    return Objects.equals(productSku, that.productSku) && Objects.equals(orderCode, that.orderCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productSku, orderCode);
  }
}

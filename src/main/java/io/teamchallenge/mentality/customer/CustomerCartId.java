package io.teamchallenge.mentality.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;

@Getter
@Embeddable
public class CustomerCartId implements Serializable {

  @Column
  private Integer customerId;

  @Column
  private Integer productId;

  public CustomerCartId(Integer customerId, Integer productId) {
    this.customerId = customerId;
    this.productId = productId;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CustomerCartId that)) {
      return false;
    }
    return Objects.equals(customerId, that.customerId) && Objects.equals(productId, that.productId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerId, productId);
  }
}

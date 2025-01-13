package io.teamchallenge.mentality.shoppingcart;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class CustomerCartId implements Serializable {

  @Column
  private String customerEmail;

  @Column
  private String productSku;

  public CustomerCartId(String customerEmail, String productSku) {
    this.customerEmail = customerEmail;
    this.productSku = productSku;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CustomerCartId that)) {
      return false;
    }
    return Objects.equals(customerEmail, that.customerEmail)
        && Objects.equals(productSku, that.productSku);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerEmail, productSku);
  }
}

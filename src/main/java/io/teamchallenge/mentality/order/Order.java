package io.teamchallenge.mentality.order;

import io.hypersistence.utils.hibernate.type.money.MonetaryAmountType;
import io.teamchallenge.mentality.customer.Customer;
import io.teamchallenge.mentality.product.Product;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.money.MonetaryAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CompositeType;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @Transient
  @CompositeType(MonetaryAmountType.class)
  @AttributeOverride(name = "amount", column = @Column(name = "price_amount"))
  @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
  private MonetaryAmount price;

  @Builder.Default
  private LocalDateTime timestamp = LocalDateTime.now();

  @ManyToOne(fetch = FetchType.LAZY)
  private Customer customer;

  @Builder.Default
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> items = new ArrayList<>();

  public Order addProduct(Product product) {
    OrderItem item = new OrderItem(product, this);
    items.add(item);
    product.getItems().add(item);
    return this;
  }

  public Order removeProduct(Product product) {
    for (Iterator<OrderItem> iterator = items.iterator(); iterator.hasNext(); ) {
      OrderItem item = iterator.next();
      if (item.getOrder().equals(this) && item.getProduct().equals(product)) {
        iterator.remove();
        item.getProduct().getItems().remove(item);
        item.setOrder(null);
        item.setProduct(null);
        break;
      }
    }
    return this;
  }
}

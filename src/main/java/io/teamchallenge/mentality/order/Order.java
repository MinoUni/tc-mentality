package io.teamchallenge.mentality.order;

import io.hypersistence.utils.hibernate.type.money.MonetaryAmountType;
import io.teamchallenge.mentality.customer.Customer;
import io.teamchallenge.mentality.notification.Notification;
import io.teamchallenge.mentality.payment.PaymentDetails;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

  /*
    @NaturalId
    @Column(nullable = false, unique = true)
    private String orderCode;
  */

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @Transient
  @CompositeType(MonetaryAmountType.class)
  @AttributeOverride(name = "amount", column = @Column(name = "price_amount"))
  private MonetaryAmount price;

  @Builder.Default
  private LocalDateTime orderDate = LocalDateTime.now();

  @ManyToOne(fetch = FetchType.LAZY)
  private Customer customer;

  @ManyToOne(fetch = FetchType.LAZY)
  private PaymentDetails payment;

  @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
  private Notification notification;

  @Builder.Default
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> items = new ArrayList<>();
}

package io.teamchallenge.mentality.payment;

import io.hypersistence.utils.hibernate.type.money.MonetaryAmountType;
import io.teamchallenge.mentality.order.Order;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "payment_details")
public class PaymentDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Transient
  @CompositeType(MonetaryAmountType.class)
  @AttributeOverride(name = "amount", column = @Column(name = "price_amount"))
  @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
  private MonetaryAmount price;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentMethod paymentMethod;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentStatus status;

  @Builder.Default
  private LocalDateTime paymentDate = LocalDateTime.now();

  @Builder.Default
  @OneToMany(fetch = FetchType.LAZY)
  private List<Order> orders = new ArrayList<>();
}

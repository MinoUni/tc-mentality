package io.teamchallenge.mentality.payment;

import io.hypersistence.utils.hibernate.type.money.MonetaryAmountType;
import io.teamchallenge.mentality.order.Order;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
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
  private Integer id;

  @Transient
  @CompositeType(MonetaryAmountType.class)
  @AttributeOverride(name = "amount", column = @Column(name = "price_amount"))
  @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
  private MonetaryAmount price;

  private String paymentProvider;

  private String status;

  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();

  @Builder.Default
  private LocalDateTime updatedAt = LocalDateTime.now();

  @MapsId
  @OneToOne(fetch = FetchType.LAZY)
  private Order order;
}

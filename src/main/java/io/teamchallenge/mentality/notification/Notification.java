package io.teamchallenge.mentality.notification;

import io.teamchallenge.mentality.order.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {

  @Id
  @Column(name = "order_code")
  private String id;

  @MapsId
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_code", referencedColumnName = "orderCode")
  private Order order;

  @Column(nullable = false)
  private String sender;

  @Column(nullable = false)
  private String recipient;

  @Column(columnDefinition = "TEXT")
  private String content;

  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();

  @Builder.Default
  private LocalDateTime sendAt = LocalDateTime.now();
}

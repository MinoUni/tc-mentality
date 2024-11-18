package io.teamchallenge.mentality.notification;

import io.teamchallenge.mentality.order.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
  private Integer id;

  @MapsId
  @OneToOne(fetch = FetchType.LAZY)
  private Order order;

  @Column(nullable = false)
  private String sender;

  @Column(nullable = false)
  private String recipient;

  private String content;

  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();

  @Builder.Default
  private LocalDateTime sendAt = LocalDateTime.now();
}

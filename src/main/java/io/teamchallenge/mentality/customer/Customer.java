package io.teamchallenge.mentality.customer;

import io.teamchallenge.mentality.order.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NaturalId
  @Column(nullable = false, unique = true)
  private String email;

  @Column(length = 50)
  private String firstName;

  @Column(length = 50)
  private String lastName;

  @Column(length = 17)
  private String phone;

  private String address;

  private String profilePicture;

  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();

  @Builder.Default
  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Order> orders = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CustomerCart> shoppingCarts = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Customer customer)) {
      return false;
    }
    return Objects.equals(email, customer.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email);
  }
}

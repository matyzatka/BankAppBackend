package mzatka.bankappbackend.models.entities;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@RequiredArgsConstructor
@Entity
public class VerificationToken {

  private static final int EXPIRATION_TIME = 1;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String token;

  @OneToOne(targetEntity = Customer.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "customer_id")
  private Customer customer;

  public VerificationToken(String token, Customer customer) {
    this.token = token;
    this.customer = customer;
  }
}

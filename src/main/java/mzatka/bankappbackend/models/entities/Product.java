package mzatka.bankappbackend.models.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import mzatka.bankappbackend.models.enums.ProductType;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

@Entity
@Inheritance(strategy = SINGLE_TABLE)
@Getter
@Setter
@Table(name = "products")
public abstract class Product {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "product_id")
  private Long id;

  @Column(name = "product_type")
  @Enumerated(EnumType.STRING)
  private ProductType productType;

  @ManyToOne private Account account;

  @JsonProperty(value = "IBAN")
  private String IBAN;

  private Double balance;
  private Double interest;
}

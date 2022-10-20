package mzatka.bankappbackend.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import mzatka.bankappbackend.models.enums.ProductType;
import mzatka.bankappbackend.utilities.BigDecimalConverter;

import javax.persistence.*;
import java.math.BigDecimal;

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
  @JsonIgnore
  private Long id;

  @Column(name = "product_type")
  @Enumerated(EnumType.STRING)
  private ProductType productType;

  @JsonIgnore @ManyToOne private Account account;

  @JsonProperty(value = "IBAN")
  private String IBAN;

  @Convert(converter = BigDecimalConverter.class)
  @Column(name = "balance")
  private BigDecimal balance;

  private String currency = "EUR";

  @JsonIgnore private BigDecimal interestRate;

  private String interestRating;
}

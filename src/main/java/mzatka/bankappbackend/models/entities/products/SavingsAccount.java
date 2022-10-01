package mzatka.bankappbackend.models.entities.products;

import mzatka.bankappbackend.models.entities.Product;

import javax.persistence.Entity;

import java.math.BigDecimal;

import static mzatka.bankappbackend.models.enums.ProductType.SAVINGS_ACCOUNT;

@Entity
public class SavingsAccount extends Product {

  public SavingsAccount() {
    this.setProductType(SAVINGS_ACCOUNT);
    this.setBalance(new BigDecimal("1000.00"));
    this.setInterestRate(new BigDecimal("1.01"));
  }
}

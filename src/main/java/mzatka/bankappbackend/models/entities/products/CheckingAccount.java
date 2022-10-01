package mzatka.bankappbackend.models.entities.products;

import mzatka.bankappbackend.models.entities.Product;

import javax.persistence.Entity;

import java.math.BigDecimal;

import static mzatka.bankappbackend.models.enums.ProductType.CHECKING_ACCOUNT;

@Entity
public class CheckingAccount extends Product {

  public CheckingAccount() {
    this.setProductType(CHECKING_ACCOUNT);
    this.setBalance(new BigDecimal("999.99"));
    this.setInterestRate(new BigDecimal("0.00"));
  }
}

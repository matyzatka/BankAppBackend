package mzatka.bankappbackend.models.entities.products;

import mzatka.bankappbackend.models.entities.Product;

import javax.persistence.Entity;

import static mzatka.bankappbackend.models.enums.ProductType.SAVINGS_ACCOUNT;

@Entity
public class SavingsAccount extends Product {

  public SavingsAccount() {
    this.setProductType(SAVINGS_ACCOUNT);
    this.setBalance(0.0);
    this.setInterest(1.1);
  }
}

package mzatka.bankappbackend.models.entities.products;

import mzatka.bankappbackend.models.entities.Product;

import javax.persistence.Entity;

import static mzatka.bankappbackend.models.enums.ProductType.CHECKING_ACCOUNT;

@Entity
public class CheckingAccount extends Product {

  public CheckingAccount() {
    this.setProductType(CHECKING_ACCOUNT);
    this.setBalance(0.0);
    this.setInterest(0.0);
  }
}

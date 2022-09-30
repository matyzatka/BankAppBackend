package mzatka.bankappbackend.models.entities.products;

import mzatka.bankappbackend.models.entities.Product;

import javax.persistence.Entity;

import static mzatka.bankappbackend.models.enums.ProductType.CREDIT_CARD;

@Entity
public class CreditCard extends Product {

  public CreditCard() {
    this.setProductType(CREDIT_CARD);
    this.setBalance(0.0);
    this.setInterest(0.0);
  }
}

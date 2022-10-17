package mzatka.bankappbackend.models.entities.products;

import mzatka.bankappbackend.models.entities.Product;

import javax.persistence.Entity;
import java.math.BigDecimal;

import static mzatka.bankappbackend.models.enums.ProductType.CREDIT_CARD;
import static mzatka.bankappbackend.utilities.PinUtilities.generatePin;

@Entity
public class CreditCard extends Product {

  private String pinCode;

  public CreditCard() {
    this.setProductType(CREDIT_CARD);
    this.setBalance(new BigDecimal("0.00"));
    this.setInterestRate(new BigDecimal("0.00"));
    this.setPinCode(generatePin());
  }

  public String getPinCode() {
    return pinCode;
  }

  public void setPinCode(String pinCode) {
    this.pinCode = pinCode;
  }
}

package mzatka.bankappbackend.models.enums;

import mzatka.bankappbackend.models.entities.Product;
import mzatka.bankappbackend.models.entities.products.CheckingAccount;
import mzatka.bankappbackend.models.entities.products.CreditCard;
import mzatka.bankappbackend.models.entities.products.DebitCard;
import mzatka.bankappbackend.models.entities.products.SavingsAccount;

import java.util.function.Supplier;

public enum ProductType {
  CHECKING_ACCOUNT("Checking Account", CheckingAccount::new),
  DEBIT_CARD("Debit Card", DebitCard::new),
  SAVINGS_ACCOUNT("Savings Account", SavingsAccount::new),
  CREDIT_CARD("Credit Card", CreditCard::new);

  private final String typeInString;
  private final Supplier<Product> supplier;

  ProductType(String typeInString, Supplier<Product> supplier) {
    this.typeInString = typeInString;
    this.supplier = supplier;
  }

  public Product getInstance() {
    return supplier.get();
  }

  public String getTypeInString() {
    return typeInString;
  }
}

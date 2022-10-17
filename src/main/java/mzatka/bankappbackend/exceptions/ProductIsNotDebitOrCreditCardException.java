package mzatka.bankappbackend.exceptions;

public class ProductIsNotDebitOrCreditCardException extends RuntimeException {

  public ProductIsNotDebitOrCreditCardException(String message) {
    super(message);
  }
}

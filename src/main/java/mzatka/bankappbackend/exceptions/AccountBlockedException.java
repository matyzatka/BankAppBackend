package mzatka.bankappbackend.exceptions;

public class AccountBlockedException extends RuntimeException {

  public AccountBlockedException(String message) {
    super(message);
  }
}

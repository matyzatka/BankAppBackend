package mzatka.bankappbackend.exceptions;

public class AccountAlreadyBlockedException extends RuntimeException {

  public AccountAlreadyBlockedException(String message) {
    super(message);
  }
}

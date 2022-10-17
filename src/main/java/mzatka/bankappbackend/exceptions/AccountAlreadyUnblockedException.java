package mzatka.bankappbackend.exceptions;

public class AccountAlreadyUnblockedException extends RuntimeException {

  public AccountAlreadyUnblockedException(String message) {
    super(message);
  }
}

package mzatka.bankappbackend.exceptions;

public class NoSuchProductWithIbanException extends RuntimeException {

  public NoSuchProductWithIbanException(String message) {
    super(message);
  }
}

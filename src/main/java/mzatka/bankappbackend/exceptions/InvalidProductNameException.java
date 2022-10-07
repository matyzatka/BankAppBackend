package mzatka.bankappbackend.exceptions;

public class InvalidProductNameException extends RuntimeException {

  public InvalidProductNameException(String message) {
    super(message);
  }
}

package mzatka.bankappbackend.exceptions;

public class IncorrectPasswordException extends RuntimeException {

  public IncorrectPasswordException(String message) {
    super(message);
  }
}

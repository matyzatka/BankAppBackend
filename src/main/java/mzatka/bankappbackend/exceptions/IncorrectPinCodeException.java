package mzatka.bankappbackend.exceptions;

public class IncorrectPinCodeException extends RuntimeException {

  public IncorrectPinCodeException(String message) {
    super(message);
  }
}

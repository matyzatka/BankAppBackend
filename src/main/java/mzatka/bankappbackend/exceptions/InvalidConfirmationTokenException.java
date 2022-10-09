package mzatka.bankappbackend.exceptions;

public class InvalidConfirmationTokenException extends RuntimeException {

  public InvalidConfirmationTokenException(String message) {
    super(message);
  }
}

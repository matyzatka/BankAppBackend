package mzatka.bankappbackend.exceptions;

public class UnknownErrorException extends RuntimeException {

  public UnknownErrorException(String message) {
    super(message);
  }
}

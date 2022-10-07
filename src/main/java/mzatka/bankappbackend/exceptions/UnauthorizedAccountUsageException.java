package mzatka.bankappbackend.exceptions;

public class UnauthorizedAccountUsageException extends RuntimeException {

  public UnauthorizedAccountUsageException(String message) {
    super(message);
  }
}

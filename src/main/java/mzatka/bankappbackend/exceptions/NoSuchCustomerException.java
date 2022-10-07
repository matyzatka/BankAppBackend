package mzatka.bankappbackend.exceptions;

public class NoSuchCustomerException extends RuntimeException {

  public NoSuchCustomerException(String message) {
    super(message);
  }
}

package mzatka.bankappbackend.advice;

import mzatka.bankappbackend.exceptions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CustomExceptionHandlerTest {

  @Autowired private CustomExceptionHandler customExceptionHandler;

  @Test
  void handleCustomerAlreadyExistsException() {
    assertNotNull(
        customExceptionHandler.handleCustomerAlreadyExistsException(
            new CustomerAlreadyExistsException("msg")));
  }

  @Test
  void handleIncorrectPasswordException() {
    assertNotNull(
        customExceptionHandler.handleIncorrectPasswordException(
            new IncorrectPasswordException("msg")));
  }

  @Test
  void handleInsufficientFundsException() {
    assertNotNull(
        customExceptionHandler.handleInsufficientFundsException(
            new InsufficientFundsException("msg")));
  }

  @Test
  void handleInvalidProductNameException() {
    assertNotNull(
        customExceptionHandler.handleInvalidProductNameException(
            new InvalidProductNameException("msg")));
  }

  @Test
  void handleNoSuchCustomerException() {
    assertNotNull(
        customExceptionHandler.handleNoSuchCustomerException(new NoSuchCustomerException("msg")));
  }

  @Test
  void handleNoSuchProductWithIbanException() {
    assertNotNull(
        customExceptionHandler.handleNoSuchProductWithIbanException(
            new NoSuchProductWithIbanException("msg")));
  }

  @Test
  void handleUnauthorizedAccountUsageException() {
    assertNotNull(
        customExceptionHandler.handleUnauthorizedAccountUsageException(
            new UnauthorizedAccountUsageException("msg")));
  }

  @Test
  void handleUnknownErrorException() {
    assertNotNull(
        customExceptionHandler.handleUnknownErrorException(new UnknownErrorException("msg")));
  }

  @Test
  void handleInvalidConfirmationTokenException() {
    assertNotNull(
        customExceptionHandler.handleInvalidConfirmationTokenException(
            new InvalidConfirmationTokenException("msg")));
  }
}

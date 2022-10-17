package mzatka.bankappbackend.advice;

import mzatka.bankappbackend.exceptions.*;
import mzatka.bankappbackend.models.dtos.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static mzatka.bankappbackend.exceptions.ExceptionMessageConfiguration.*;

@RestControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(CustomerAlreadyExistsException.class)
  @ResponseStatus(value = HttpStatus.CONFLICT)
  public ErrorResponseDto handleCustomerAlreadyExistsException(CustomerAlreadyExistsException e) {
    return new ErrorResponseDto(CUSTOMER_ALREADY_EXISTS_EXCEPTION_MESSAGE, e.getMessage());
  }

  @ExceptionHandler(IncorrectPasswordException.class)
  @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
  public ErrorResponseDto handleIncorrectPasswordException(IncorrectPasswordException e) {
    return new ErrorResponseDto(INCORRECT_PASSWORD_EXCEPTION_MESSAGE, e.getMessage());
  }

  @ExceptionHandler(InsufficientFundsException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ErrorResponseDto handleInsufficientFundsException(InsufficientFundsException e) {
    return new ErrorResponseDto(INSUFFICIENT_FUNDS_EXCEPTION_MESSAGE, e.getMessage());
  }

  @ExceptionHandler(InvalidProductNameException.class)
  @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
  public ErrorResponseDto handleInvalidProductNameException(InvalidProductNameException e) {
    return new ErrorResponseDto(INVALID_PRODUCT_NAME_EXCEPTION_MESSAGE, e.getMessage());
  }

  @ExceptionHandler(NoSuchCustomerException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ErrorResponseDto handleNoSuchCustomerException(NoSuchCustomerException e) {
    return new ErrorResponseDto(NO_SUCH_CUSTOMER_EXCEPTION_MESSAGE, e.getMessage());
  }

  @ExceptionHandler(NoSuchProductWithIbanException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ErrorResponseDto handleNoSuchProductWithIbanException(NoSuchProductWithIbanException e) {
    return new ErrorResponseDto(NO_SUCH_PRODUCT_WITH_IBAN_EXCEPTION_MESSAGE, e.getMessage());
  }

  @ExceptionHandler(UnauthorizedAccountUsageException.class)
  @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
  public ErrorResponseDto handleUnauthorizedAccountUsageException(
      UnauthorizedAccountUsageException e) {
    return new ErrorResponseDto(UNAUTHORIZED_ACCOUNT_USAGE_EXCEPTION_MESSAGE, e.getMessage());
  }

  @ExceptionHandler(UnknownErrorException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponseDto handleUnknownErrorException(UnknownErrorException e) {
    return new ErrorResponseDto(UNKNOWN_ERROR_EXCEPTION_MESSAGE, e.getMessage());
  }

  @ExceptionHandler(InvalidConfirmationTokenException.class)
  @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
  public ErrorResponseDto handleInvalidConfirmationTokenException(
      InvalidConfirmationTokenException e) {
    return new ErrorResponseDto(INVALID_CONFIRMATION_TOKEN_EXCEPTION_MESSAGE, e.getMessage());
  }

  @ExceptionHandler(AccountAlreadyBlockedException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ErrorResponseDto handleAccountAlreadyBlockedException(AccountAlreadyBlockedException e) {
    return new ErrorResponseDto(ACCOUNT_ALREADY_BLOCKED_EXCEPTION_MESSAGE, e.getMessage());
  }

  @ExceptionHandler(AccountAlreadyUnblockedException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ErrorResponseDto handleAccountAlreadyUnblockedException(
      AccountAlreadyUnblockedException e) {
    return new ErrorResponseDto(ACCOUNT_ALREADY_UNBLOCKED_EXCEPTION_MESSAGE, e.getMessage());
  }

  @ExceptionHandler(IncorrectPinCodeException.class)
  @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
  public ErrorResponseDto handleIncorrectPinCodeException(IncorrectPinCodeException e) {
    return new ErrorResponseDto(INCORRECT_PIN_CODE_EXCEPTION_MESSAGE, e.getMessage());
  }

  @ExceptionHandler(ProductIsNotDebitOrCreditCardException.class)
  @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
  public ErrorResponseDto handleProductIsNotDebitOrCreditCardException(
      ProductIsNotDebitOrCreditCardException e) {
    return new ErrorResponseDto(
        PRODUCT_IS_NOT_A_CREDIT_OR_DEBIT_CARD_EXCEPTION_MESSAGE, e.getMessage());
  }
}

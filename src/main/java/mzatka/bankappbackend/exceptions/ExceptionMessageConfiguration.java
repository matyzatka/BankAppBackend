package mzatka.bankappbackend.exceptions;

public final class ExceptionMessageConfiguration {

  public static final String NO_SUCH_PRODUCT_WITH_IBAN_EXCEPTION_MESSAGE =
      "No such product with this IBAN.";

  public static final String NO_SUCH_CUSTOMER_EXCEPTION_MESSAGE =
      "No such customer with this username or id.";

  public static final String INSUFFICIENT_FUNDS_EXCEPTION_MESSAGE = "Insufficient funds.";

  public static final String CUSTOMER_ALREADY_EXISTS_EXCEPTION_MESSAGE =
      "Customer with this username or email already exists in database.";

  public static final String INCORRECT_PASSWORD_EXCEPTION_MESSAGE = "Password is not correct.";

  public static final String UNAUTHORIZED_ACCOUNT_USAGE_EXCEPTION_MESSAGE =
      "You are not authorized to send money from this account.";

  public static final String INVALID_PRODUCT_NAME_EXCEPTION_MESSAGE =
      "Invalid product name. Choose \"creditCard\" or \"savingsAccount\".";

  public static final String UNKNOWN_ERROR_EXCEPTION_MESSAGE = "Unknown error. Try again.";

  public static final String INVALID_CONFIRMATION_TOKEN_EXCEPTION_MESSAGE =
      "Confirmation token is not valid.";

  public static final String ACCOUNT_ALREADY_BLOCKED_EXCEPTION_MESSAGE =
      "Account is already blocked.";

  public static final String ACCOUNT_ALREADY_UNBLOCKED_EXCEPTION_MESSAGE =
      "Account is already unblocked.";

  public static final String INCORRECT_PIN_CODE_EXCEPTION_MESSAGE = "Pin code is not correct.";

  public static final String PRODUCT_IS_NOT_A_CREDIT_OR_DEBIT_CARD_EXCEPTION_MESSAGE =
      "Product is not a debit or credit card.";

  public static final String ACCOUNT_BLOCKED_EXCEPTION_MESSAGE = "Account blocked.";
}

package mzatka.bankappbackend.configuration;

public final class ExceptionMessageConfiguration {

    public static final String NO_SUCH_PRODUCT_WITH_IBAN_EXCEPTION_MESSAGE = "No such product with this IBAN.";

    public static final String NO_SUCH_CUSTOMER_EXCEPTION_MESSAGE = "No such customer with this username or id.";

    public static final String INSUFFICIENT_FUNDS_EXCEPTION_MESSAGE = "Insufficient funds.";

    public static final String CUSTOMER_ALREADY_EXISTS_EXCEPTION_MESSAGE = "Customer with this username or email already exists in database.";

    public static final String INCORRECT_PASSWORD_EXCEPTION_MESSAGE = "Password is not correct.";

    public static final String UNAUTHORIZED_ACCOUNT_USAGE_EXCEPTION_MESSAGE = "You are not authorized to send money from this account.";

    public static final String INVALID_PRODUCT_NAME_EXCEPTION_MESSAGE = "Invalid product name. Choose \"creditCard\" or \"savingsAccount\".";

    public static final String UNKNOWN_ERROR_EXCEPTION_MESSAGE = "Unknown error. Try again.";
}

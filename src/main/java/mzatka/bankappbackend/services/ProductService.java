package mzatka.bankappbackend.services;

import mzatka.bankappbackend.models.dtos.AllTransactionLogsDto;
import mzatka.bankappbackend.models.dtos.TransactionDto;
import mzatka.bankappbackend.models.dtos.TransferDto;
import mzatka.bankappbackend.models.entities.Account;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.models.entities.Product;
import mzatka.bankappbackend.models.enums.ProductType;

public interface ProductService {

  Product createProduct(ProductType productType, Account account);

  boolean addedProduct(String productName, Customer customer);

  boolean deletedProduct(Customer customer, String iban);

  void creditTheInterestOnSavingsAccounts();

  Product getProductByIban(String iban);

  boolean ibanNotExists(String iban);

  boolean hasSufficientFunds(TransferDto transferDto);

  boolean hasSufficientFunds(TransactionDto transactionDto);

  double depositCashAndReturnBalance(TransferDto transferDto);

  double withdrawCashAndReturnBalance(TransferDto transferDto);

  boolean productNotBelongsToLoggedCustomer(String iban, Customer customer);

  boolean transactionCompleted(TransactionDto transactionDto);

  void createTransactionLog(Product sender, Product receiver, Double amount);

  AllTransactionLogsDto getAllTransactionLogsForCustomer(Customer customer);

  void synchronizeAccount(Product product);
}

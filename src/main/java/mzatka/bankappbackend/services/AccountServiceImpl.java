package mzatka.bankappbackend.services;

import mzatka.bankappbackend.models.entities.Account;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static mzatka.bankappbackend.models.enums.ProductType.*;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

  private final ProductService productService;
  private final AccountRepository accountRepository;

  public AccountServiceImpl(ProductService productService, AccountRepository accountRepository) {
    this.productService = productService;
    this.accountRepository = accountRepository;
  }

  @Override
  public Account createNewAccount(Customer customer) {
    Account account = new Account(customer);
    account.getProducts().add(productService.createProduct(CHECKING_ACCOUNT, account));
//    account.getProducts().add(productService.createProduct(SAVINGS_ACCOUNT, account));
    BigDecimal checkingAccountBalance =
        account.getProducts().stream()
            .filter(product -> product.getProductType().equals(CHECKING_ACCOUNT))
            .findFirst()
            .get()
            .getBalance();
    account.getProducts().stream()
        .filter(product -> product.getProductType().equals(DEBIT_CARD))
        .forEach(product -> product.setBalance(checkingAccountBalance));
    accountRepository.save(account);
    return account;
  }
}

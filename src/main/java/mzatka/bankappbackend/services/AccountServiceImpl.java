package mzatka.bankappbackend.services;

import lombok.RequiredArgsConstructor;
import mzatka.bankappbackend.models.entities.Account;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;

import static mzatka.bankappbackend.models.enums.ProductType.CHECKING_ACCOUNT;
import static mzatka.bankappbackend.models.enums.ProductType.DEBIT_CARD;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

  private final ProductService productService;
  private final AccountRepository accountRepository;

  @Override
  public Account createNewAccount(Customer customer) {
    Account account = new Account(customer);
    accountRepository.save(account);
    account
        .getProducts()
        .addAll(
            Arrays.asList(
                productService.createProduct(CHECKING_ACCOUNT, account),
                productService.createProduct(DEBIT_CARD, account)));
    accountRepository.save(account);
    return account;
  }
}

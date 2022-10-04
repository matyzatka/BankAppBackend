package mzatka.bankappbackend.services;

import mzatka.bankappbackend.models.entities.Account;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AccountServiceImplTest {

  @Autowired private AccountRepository accountRepository;
  @Autowired private AccountService accountService;

  @Test
  public void returns_brand_new_account() {
    int oldAccounts = accountRepository.findAll().size();
    Account account = accountService.createNewAccount(new Customer());
    assertNotNull(account);
    assertEquals(1, accountRepository.findAll().size() - oldAccounts);
  }
}

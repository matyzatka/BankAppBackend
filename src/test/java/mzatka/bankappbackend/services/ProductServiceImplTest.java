package mzatka.bankappbackend.services;

import mzatka.bankappbackend.models.entities.Account;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.models.entities.Product;
import mzatka.bankappbackend.models.factories.ProductFactory;
import mzatka.bankappbackend.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static mzatka.bankappbackend.models.enums.ProductType.CHECKING_ACCOUNT;
import static mzatka.bankappbackend.models.enums.ProductType.CREDIT_CARD;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceImplTest {

  @Autowired CustomerService customerService;
  @Autowired ProductService productService;
  @Autowired AccountService accountService;
  @Autowired AccountRepository accountRepository;

  @Test
  public void sets_formatted_interest_rating() {
    Product product = ProductFactory.createProduct(CHECKING_ACCOUNT);
    product.setInterestRating(product.getInterestRate().multiply(BigDecimal.valueOf(100)) + "%");
    assertNotNull(product.getInterestRating());
  }

  @Test
  public void returns_true_if_adds_product() {
    customerService.saveCustomer(new Customer("customer"));
    Customer customer = customerService.getCustomerByUsername("customer");
    customer.setAccount(accountService.createNewAccount(customer));
    assertTrue(productService.addedProduct("creditCard", customer));
    assertTrue(productService.addedProduct("savingsAccount", customer));
    assertFalse(productService.addedProduct("whatever", customer));
  }

  @Test
  public void returns_true_if_deletes_product() {
    customerService.saveCustomer(new Customer("next customer"));
    Customer customer = customerService.getCustomerByUsername("next customer");
    customer.setAccount(accountService.createNewAccount(customer));
    String testIban = "00000000/0000";
    customer.getAccount().getProducts().stream().findAny().orElseThrow().setIBAN(testIban);
    assertTrue(productService.deletedProduct(customer, testIban));
  }

  @Test
  public void if_no_saving_accounts_nothing_happens() {
    productService.creditTheInterestOnSavingsAccounts();
  }

  @Test
  public void returns_product_by_iban() {
    String testIban = "0000000/0000";
    Customer customer = new Customer("Fred");
    customerService.saveCustomer(customer);
    Account account = accountService.createNewAccount(customer);
    accountRepository.save(account);
    Product product = productService.createProduct(CREDIT_CARD, account);
    product.setIBAN(testIban);
    assertNotNull(productService.getProductByIban(testIban));
  }

  @Test
  public void returns_true_if_IBAN_not_exists() {
    String iban = "non-existing";
    assertTrue(productService.ibanNotExists(iban));
  }
}

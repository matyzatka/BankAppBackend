package mzatka.bankappbackend.services;

import mzatka.bankappbackend.models.dtos.TransactionDto;
import mzatka.bankappbackend.models.dtos.TransferDto;
import mzatka.bankappbackend.models.entities.Account;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.models.entities.Product;
import mzatka.bankappbackend.models.enums.ProductType;
import mzatka.bankappbackend.models.factories.ProductFactory;
import mzatka.bankappbackend.repositories.ProductRepository;
import mzatka.bankappbackend.utilities.IbanUtilities;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static mzatka.bankappbackend.models.enums.Currency.CZK;
import static mzatka.bankappbackend.models.enums.ProductType.CREDIT_CARD;
import static mzatka.bankappbackend.models.enums.ProductType.SAVINGS_ACCOUNT;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final IbanUtilities ibanUtilities;

  public ProductServiceImpl(ProductRepository productRepository, IbanUtilities ibanUtilities) {
    this.productRepository = productRepository;
    this.ibanUtilities = ibanUtilities;
  }

  @Override
  public Product createProduct(ProductType productType, Account account) {
    Product product = ProductFactory.createProduct(productType);
    product.setAccount(account);
    product.setCurrency(CZK);
    if (!Objects.equals(product.getInterestRate(), new BigDecimal("0.00"))) {
      product.setInterestRating(
          (product
                  .getInterestRate()
                  .multiply(BigDecimal.valueOf(100))
                  .subtract(BigDecimal.valueOf(100)))
              + "%");
    } else {
      product.setInterestRating((product.getInterestRate().doubleValue() * 100) + "%");
    }
    String iban;
    do {
      iban = ibanUtilities.generateIBAN();
    } while (!ibanUtilities.isIbanAvailable(iban));
    product.setIBAN(iban);
    return productRepository.save(product);
  }

  @Override
  public boolean addedProduct(String productName, Customer customer) {
    if (productName.equalsIgnoreCase("creditCard")) {
      customer.getAccount().getProducts().add(createProduct(CREDIT_CARD, customer.getAccount()));
      return true;
    }
    if (productName.equalsIgnoreCase("savingsAccount")) {
      customer
          .getAccount()
          .getProducts()
          .add(createProduct(SAVINGS_ACCOUNT, customer.getAccount()));
      return true;
    }
    return false;
  }

  @Override
  public boolean deletedProduct(Customer customer, String iban) {
    List<Product> products = customer.getAccount().getProducts();
    return products.removeIf(product -> product.getIBAN().equals(iban));
  }

  @Override
  @Scheduled(initialDelay = 10000, fixedRate = 10000)
  public void creditTheInterestOnSavingsAccounts() {
    try {
      productRepository.findAll().stream()
          .filter(product -> product.getProductType().equals(SAVINGS_ACCOUNT))
          .forEach(
              product ->
                  product.setBalance(product.getBalance().multiply(product.getInterestRate())));
    } catch (Exception e) {
      System.err.println("Error occurred, when trying to increase amounts on saving accounts.");
      System.out.println(e.getMessage());
    }
  }

  @Override
  public Product getProductByIban(String iban) {
    return productRepository.findAll().stream()
        .filter(product -> product.getIBAN().equals(iban))
        .findFirst()
        .orElse(null);
  }

  @Override
  public boolean ibanNotExists(String iban) {
    return getProductByIban(iban) == null;
  }

  @Override
  public boolean hasSufficientFunds(TransferDto transferDto) {
    double remainder =
        getProductByIban(transferDto.getIban())
            .getBalance()
            .subtract(BigDecimal.valueOf(transferDto.getAmount()))
            .doubleValue();
    return remainder >= 0;
  }

  @Override
  public boolean hasSufficientFunds(TransactionDto transactionDto) {
    double remainder =
        getProductByIban(transactionDto.getSendingIban())
            .getBalance()
            .subtract(BigDecimal.valueOf(transactionDto.getAmount()))
            .doubleValue();
    return remainder >= 0;
  }

  @Override
  public double depositCashAndReturnBalance(TransferDto transferDto) {
    Product product = getProductByIban(transferDto.getIban());
    product.setBalance(product.getBalance().add(BigDecimal.valueOf(transferDto.getAmount())));
    productRepository.save(product);
    return product.getBalance().doubleValue();
  }

  @Override
  public double withdrawCashAndReturnBalance(TransferDto transferDto) {
    Product product = getProductByIban(transferDto.getIban());
    product.setBalance(product.getBalance().subtract(BigDecimal.valueOf(transferDto.getAmount())));
    productRepository.save(product);
    return product.getBalance().doubleValue();
  }

  @Override
  public boolean productNotBelongsToLoggedCustomer(String iban, Customer customer) {
    return !getProductByIban(iban).getAccount().getCustomer().equals(customer);
  }

  @Override
  public boolean transactionCompleted(TransactionDto transactionDto) {
    try {
      Product sender = getProductByIban(transactionDto.getSendingIban());
      Product receiver = getProductByIban(transactionDto.getReceivingIban());
      sender.setBalance(
          sender.getBalance().subtract(BigDecimal.valueOf(transactionDto.getAmount())));
      receiver.setBalance(
          receiver.getBalance().add(BigDecimal.valueOf(transactionDto.getAmount())));
      productRepository.saveAll(List.of(sender, receiver));
      return true;
    } catch (Exception e) {
      System.err.println(e.getMessage());
      return false;
    }
  }
}

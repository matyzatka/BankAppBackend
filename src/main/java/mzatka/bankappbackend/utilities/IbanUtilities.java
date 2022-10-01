package mzatka.bankappbackend.utilities;

import mzatka.bankappbackend.models.entities.Account;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class IbanUtilities {

  private final CustomerRepository customerRepository;

  public IbanUtilities(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  public boolean isIbanAvailable(String iban) {
    return customerRepository.findAll().stream()
        .map(Customer::getAccount)
        .map(Account::getProducts)
        .flatMap(Collection::stream)
        .noneMatch(product -> product.getIBAN().equals(iban));
  }

  public String generateIBAN() {
    return IntStream.range(0, 10)
        .mapToObj(i -> String.valueOf((int) (Math.random() * 9)))
        .collect(Collectors.joining("", "", "/0090"));
  }
}

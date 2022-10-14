package mzatka.bankappbackend.utilities;

import mzatka.bankappbackend.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class IbanUtilities {

  private final ProductRepository productRepository;

  public IbanUtilities(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public boolean isIbanAvailable(String iban) {
    assert productRepository.findAll() != null;
    return productRepository.findAll().stream()
        .noneMatch(product -> product.getIBAN().equals(iban));
  }

  public String generateIBAN() {
    return IntStream.range(0, 10)
        .mapToObj(i -> String.valueOf((int) (Math.random() * 9)))
        .collect(Collectors.joining("", "", "/0090"));
  }
}

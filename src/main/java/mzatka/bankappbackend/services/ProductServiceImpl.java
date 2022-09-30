package mzatka.bankappbackend.services;

import mzatka.bankappbackend.models.entities.Account;
import mzatka.bankappbackend.models.entities.Product;
import mzatka.bankappbackend.models.enums.ProductType;
import mzatka.bankappbackend.models.factories.ProductFactory;
import mzatka.bankappbackend.repositories.ProductRepository;
import mzatka.bankappbackend.utilities.IbanUtilities;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final IbanUtilities ibanUtilities;

  public ProductServiceImpl(ProductRepository productRepository, IbanUtilities ibanUtilities) {
    this.productRepository = productRepository;
    this.ibanUtilities = ibanUtilities;
  }

  @Override
  public Product addNewProduct(ProductType productType, Account account) {
    Product product = ProductFactory.createProduct(productType);
    product.setAccount(account);
    String iban;
      do {
        iban = ibanUtilities.generateIBAN();
      } while (!ibanUtilities.isIbanAvailable(iban));
    product.setIBAN(iban);
    return productRepository.save(product);
  }
}

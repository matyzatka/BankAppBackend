package mzatka.bankappbackend.services;

import mzatka.bankappbackend.models.entities.Account;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.models.entities.Product;
import mzatka.bankappbackend.models.enums.ProductType;

public interface ProductService {

  Product createProduct(ProductType productType, Account account);

  boolean addedProduct(String productName, Customer customer);

  boolean deletedProduct(Customer customer, String iban);
}

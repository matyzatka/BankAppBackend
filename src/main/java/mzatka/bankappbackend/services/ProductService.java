package mzatka.bankappbackend.services;

import mzatka.bankappbackend.models.entities.Account;
import mzatka.bankappbackend.models.entities.Product;
import mzatka.bankappbackend.models.enums.ProductType;

public interface ProductService {

  Product addNewProduct(ProductType productType, Account account);
}

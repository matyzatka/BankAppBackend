package mzatka.bankappbackend.models.factories;

import mzatka.bankappbackend.models.entities.Product;
import mzatka.bankappbackend.models.enums.ProductType;

public class ProductFactory {

  public static Product createProduct(ProductType productType) {
    return productType.getInstance();
  }
}

package mzatka.bankappbackend.controllers;

import mzatka.bankappbackend.services.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atm")
public class AtmController {

  private final ProductService productService;

  public AtmController(ProductService productService) {
    this.productService = productService;
  }
}

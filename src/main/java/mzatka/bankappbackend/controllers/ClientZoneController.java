package mzatka.bankappbackend.controllers;

import mzatka.bankappbackend.exceptions.*;
import mzatka.bankappbackend.models.dtos.*;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.services.CustomerService;
import mzatka.bankappbackend.services.DtoService;
import mzatka.bankappbackend.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/client-zone")
public class ClientZoneController {

  private final CustomerService customerService;
  private final ProductService productService;
  private final DtoService dtoService;

  public ClientZoneController(
      CustomerService customerService, ProductService productService, DtoService dtoService) {
    this.customerService = customerService;
    this.productService = productService;
    this.dtoService = dtoService;
  }

  @GetMapping
  public ResponseEntity<Dto> showCustomer(
      @RequestHeader(name = "Authorization") String bearerToken) {
    Customer customer = customerService.getCustomerFromAuthorizationHeader(bearerToken);
    CustomerDto customerDto = dtoService.convertToDto(customer);
    return ResponseEntity.ok(customerDto);
  }

  @PostMapping("/pay")
  public ResponseEntity<Dto> makeTransaction(
      @RequestHeader(name = "Authorization") String bearerToken,
      @RequestBody @Valid TransactionDto transactionDto) {
    Customer customer = customerService.getCustomerFromAuthorizationHeader(bearerToken);
    if (productService.ibanNotExists(transactionDto.getSendingIban())
        || productService.ibanNotExists(transactionDto.getReceivingIban())) {
      throw new NoSuchProductWithIbanException("/client-zone/pay");
    }
    if (productService.productNotBelongsToLoggedCustomer(
        transactionDto.getSendingIban(), customer)) {
      throw new UnauthorizedAccountUsageException("/client-zone/pay");
    }
    if (!productService.hasSufficientFunds(transactionDto)) {
      throw new InsufficientFundsException("/client-zone/pay");
    }
    if (productService.transactionCompleted(transactionDto)) {
      return ResponseEntity.ok(new MessageDto("Transaction successful."));
    }
    throw new UnknownErrorException("/client-zone/pay");
  }

  @PutMapping("/update")
  public ResponseEntity<Dto> updateCustomer(
      @RequestHeader(name = "Authorization") String bearerToken,
      @RequestBody @Valid NewCustomerDto customerDto) {
    Customer customer = customerService.getCustomerFromAuthorizationHeader(bearerToken);
    customerService.assignValuesToCustomer(customer, customerDto);
    customerService.saveCustomer(customer);
    return ResponseEntity.ok(new MessageDto("Customer updated successfully."));
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Dto> deleteCustomer(
      @RequestHeader(name = "Authorization") String bearerToken,
      @RequestBody @Valid ConfirmationPasswordDto confirmationPasswordDto) {
    try {
      Customer customer = customerService.getCustomerFromAuthorizationHeader(bearerToken);
      if (!customerService.passwordIsCorrect(
          confirmationPasswordDto.getPassword(), customer.getPassword())) {
        throw new IncorrectPasswordException("/client-zone/delete");
      }
      Long id = customer.getId();
      customerService.deleteCustomer(id);
      return ResponseEntity.ok(new MessageDto("Customer deleted successfully."));
    } catch (NullPointerException e) {
      throw new NoSuchCustomerException("/client-zone/delete");
    }
  }

  @PostMapping("/add-product")
  public ResponseEntity<Dto> addProductToCustomer(
      @RequestHeader(name = "Authorization") String bearerToken,
      @RequestBody @Valid AddProductDto addProductDto) {
    Customer customer = customerService.getCustomerFromAuthorizationHeader(bearerToken);
    String productName = addProductDto.getProductName();
    String inputPassword = addProductDto.getPassword();
    String storedPassword = customer.getPassword();
    if (!customerService.passwordIsCorrect(inputPassword, storedPassword)) {
      throw new IncorrectPasswordException("/client-zone/add-product");
    }
    if (productService.addedProduct(productName, customer)) {
      customerService.saveCustomer(customer);
      return ResponseEntity.ok(
          new MessageDto(
              String.format(
                  "Product {%s} successfully added to account of customer %s %s.",
                  productName, customer.getFirstName(), customer.getLastName())));
    }
    throw new InvalidProductNameException("/client-zone/add-product");
  }

  @DeleteMapping("/delete-product")
  public ResponseEntity<Dto> deleteProductByIBAN(
      @RequestHeader(name = "Authorization") String bearerToken,
      @RequestBody @Valid DeleteConfirmationDto confirmationDto) {
    Customer customer = customerService.getCustomerFromAuthorizationHeader(bearerToken);
    String iban = confirmationDto.getIban();
    String inputPassword = confirmationDto.getPassword();
    String storedPassword = customer.getPassword();
    if (!customerService.passwordIsCorrect(inputPassword, storedPassword)) {
      return ResponseEntity.badRequest().body(new MessageDto("Incorrect password."));
    }
    if (productService.deletedProduct(customer, iban)) {
      customerService.saveCustomer(customer);
      return ResponseEntity.ok(
          new MessageDto(
              String.format(
                  "Product with IBAN: %s removed successfully from account of %s %s.",
                  iban, customer.getFirstName(), customer.getLastName())));
    }
    throw new NoSuchProductWithIbanException("/client-zone/delete-product");
  }
}

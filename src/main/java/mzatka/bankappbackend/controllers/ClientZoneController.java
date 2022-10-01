package mzatka.bankappbackend.controllers;

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
    if (productService.ibanNotExists(transactionDto.getSendingIban())) {
      return ResponseEntity.badRequest().body(new MessageDto("Sender IBAN does not exist."));
    }
    if (productService.ibanNotExists(transactionDto.getReceivingIban())) {
      return ResponseEntity.badRequest().body(new MessageDto("Receiver IBAN does not exist."));
    }
    if (productService.productNotBelongsToLoggedCustomer(
        transactionDto.getSendingIban(), customer)) {
      return ResponseEntity.badRequest()
          .body(new MessageDto("You are not authorized to send money from this account."));
    }
    if (!productService.hasSufficientFunds(transactionDto)) {
      return ResponseEntity.badRequest()
          .body(new MessageDto("Insufficient funds for making a transaction."));
    }
    if (productService.transactionCompleted(transactionDto)) {
      return ResponseEntity.ok(new MessageDto("Transaction successful."));
    }
    return ResponseEntity.badRequest().body(new MessageDto("Unknown error. Try again."));
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
    Customer customer = customerService.getCustomerFromAuthorizationHeader(bearerToken);
    if (!customerService.passwordIsCorrect(
        confirmationPasswordDto.getPassword(), customer.getPassword())) {
      return ResponseEntity.badRequest().body(new MessageDto("Incorrect password."));
    }
    try {
      Long id = customer.getId();
      customerService.deleteCustomer(id);
      return ResponseEntity.ok(new MessageDto("Customer deleted successfully."));
    } catch (NullPointerException e) {
      return ResponseEntity.badRequest().body(new MessageDto("Customer does not exist anymore."));
    }
  }

  @PostMapping("/add-product/{productName}")
  public ResponseEntity<Dto> addProductToCustomer(
      @RequestHeader(name = "Authorization") String bearerToken,
      @PathVariable(name = "productName") String productName,
      @RequestBody @Valid ConfirmationPasswordDto confirmationPasswordDto) {
    Customer customer = customerService.getCustomerFromAuthorizationHeader(bearerToken);
    String inputPassword = confirmationPasswordDto.getPassword();
    String storedPassword = customer.getPassword();
    if (!customerService.passwordIsCorrect(inputPassword, storedPassword)) {
      return ResponseEntity.badRequest().body(new MessageDto("Incorrect password."));
    }
    if (productService.addedProduct(productName, customer)) {
      customerService.saveCustomer(customer);
      return ResponseEntity.ok(
          new MessageDto(
              String.format(
                  "Product [%s] successfully added to account of customer %s %s.",
                  productName, customer.getFirstName(), customer.getLastName())));
    }
    return ResponseEntity.badRequest()
        .body(new MessageDto("Invalid product name. Choose [creditCard] or [savingsAccount]."));
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
    return ResponseEntity.badRequest()
        .body(new MessageDto(String.format("No such product with IBAN: %s", iban)));
  }
}

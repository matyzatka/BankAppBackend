package mzatka.bankappbackend.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mzatka.bankappbackend.exceptions.*;
import mzatka.bankappbackend.models.dtos.*;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.services.AccountService;
import mzatka.bankappbackend.services.CustomerService;
import mzatka.bankappbackend.services.DtoService;
import mzatka.bankappbackend.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/client-zone")
@RequiredArgsConstructor
@Slf4j
public class ClientZoneController {

  private final CustomerService customerService;
  private final ProductService productService;
  private final AccountService accountService;
  private final DtoService dtoService;

  @GetMapping
  public ResponseEntity<Dto> showCustomer(
      @RequestHeader(name = "Authorization") String bearerToken,
      @RequestParam(required = false, name = "currency") String currency) {
    Customer customer = customerService.getCustomerFromAuthorizationHeader(bearerToken);
    if (accountService.isAccountBlocked(customer)) {
      throw new AccountBlockedException("/client-zone");
    }
    CustomerDto customerDto =
        currency == null
            ? dtoService.convertToDto(customer)
            : dtoService.convertToDto(customer, currency.toLowerCase());
    return ok(customerDto);
  }

  @PostMapping("/pay")
  public ResponseEntity<Dto> makeTransaction(
      @RequestHeader(name = "Authorization") String bearerToken,
      @RequestBody @Valid TransactionDto transactionDto) {
    Customer customer = customerService.getCustomerFromAuthorizationHeader(bearerToken);
    if (accountService.isAccountBlocked(customer)) {
      throw new AccountBlockedException("/client-zone/pay");
    }
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
      return ok(new MessageDto("Transaction successful."));
    }
    throw new UnknownErrorException("/client-zone/pay");
  }

  @PutMapping("/update")
  public ResponseEntity<Dto> updateCustomer(
      @RequestHeader(name = "Authorization") String bearerToken,
      @RequestBody @Valid NewCustomerDto customerDto) {
    Customer customer = customerService.getCustomerFromAuthorizationHeader(bearerToken);
    if (accountService.isAccountBlocked(customer)) {
      throw new AccountBlockedException("/client-zone/update");
    }
    customerService.assignValuesToCustomer(customer, customerDto);
    customerService.saveCustomer(customer);
    return ok(new MessageDto("Customer updated successfully."));
  }

  @PostMapping("/block")
  public ResponseEntity<Dto> blockCustomerAccount(
      @RequestHeader(name = "Authorization") String bearerToken,
      @RequestBody ConfirmationPasswordDto confirmationPasswordDto) {
    Customer customer = customerService.getCustomerFromAuthorizationHeader(bearerToken);
    if (!customerService.passwordIsCorrect(
        confirmationPasswordDto.getPassword(), customer.getPassword())) {
      throw new IncorrectPasswordException("/client-zone/block");
    }
    if (customer.getAccount().getIsBlocked()) {
      throw new AccountAlreadyBlockedException("/client-zone/block");
    }
    customer.getAccount().setIsBlocked(true);
    customerService.saveCustomer(customer);
    return ok(new MessageDto("Customer's account has been blocked successfully."));
  }

  @PostMapping("/unblock")
  public ResponseEntity<Dto> unblockCustomerAccount(
      @RequestHeader(name = "Authorization") String bearerToken,
      @RequestBody ConfirmationPasswordDto confirmationPasswordDto) {
    Customer customer = customerService.getCustomerFromAuthorizationHeader(bearerToken);
    if (!customerService.passwordIsCorrect(
        confirmationPasswordDto.getPassword(), customer.getPassword())) {
      throw new IncorrectPasswordException("/client-zone/unblock");
    }
    if (!customer.getAccount().getIsBlocked()) {
      throw new AccountAlreadyUnblockedException("/client-zone/block");
    }
    customer.getAccount().setIsBlocked(false);
    customerService.saveCustomer(customer);
    return ok(new MessageDto("Customer's account has been unblocked successfully."));
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Dto> deleteCustomer(
      @RequestHeader(name = "Authorization") String bearerToken,
      @RequestBody @Valid ConfirmationPasswordDto confirmationPasswordDto) {
    try {
      Customer customer = customerService.getCustomerFromAuthorizationHeader(bearerToken);
      if (accountService.isAccountBlocked(customer)) {
        throw new AccountBlockedException("/client-zone/delete");
      }
      if (!customerService.passwordIsCorrect(
          confirmationPasswordDto.getPassword(), customer.getPassword())) {
        throw new IncorrectPasswordException("/client-zone/delete");
      }
      Long id = customer.getId();
      customerService.deleteCustomer(id);
      return ok(new MessageDto("Customer deleted successfully."));
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
    if (accountService.isAccountBlocked(customer)) {
      throw new AccountBlockedException("/client-zone/add-product");
    }
    if (productService.addedProduct(productName, customer)) {
      customerService.saveCustomer(customer);
      return ok(
          new MessageDto(
              "Product {"
                  + productName
                  + "} successfully added to account of customer "
                  + customer.getFirstName()
                  + " "
                  + customer.getLastName()
                  + "."));
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
    if (accountService.isAccountBlocked(customer)) {
      throw new AccountBlockedException("/client-zone/delete-product");
    }
    if (productService.deletedProduct(customer, iban)) {
      customerService.saveCustomer(customer);
      return ok(
          new MessageDto(
              "Product with IBAN: "
                  + iban
                  + " removed successfully from account of "
                  + customer.getFirstName()
                  + " "
                  + customer.getLastName()
                  + "."));
    }
    throw new NoSuchProductWithIbanException("/client-zone/delete-product");
  }

  @GetMapping("/history")
  public ResponseEntity<Dto> getAllTransactionLogs(
      @RequestHeader(name = "Authorization") String bearerToken) {
    Customer customer = customerService.getCustomerFromAuthorizationHeader(bearerToken);
    if (accountService.isAccountBlocked(customer)) {
      throw new AccountBlockedException("/client-zone/history");
    }
    return ResponseEntity.ok(productService.getAllTransactionLogsForCustomer(customer));
  }
}

package mzatka.bankappbackend.controllers;

import mzatka.bankappbackend.models.dtos.*;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class EntryController {

  private final CustomerService customerService;

  public EntryController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @PostMapping("/sign-up")
  public ResponseEntity<Dto> signUp(@RequestBody @Valid NewCustomerDto newCustomerDto) {
    if (customerService.customerExistsWithUsernameOrEmail(
        newCustomerDto.getUsername(), newCustomerDto.getEmail())) {
      return ResponseEntity.status(409)
          .body(
              new MessageDto(
                  String.format(
                      "User already exists with username: %s, or email: %s.",
                      newCustomerDto.getUsername(), newCustomerDto.getEmail())));
    }
    customerService.registerNewCustomer(newCustomerDto);
    return ResponseEntity.ok(new MessageDto("Registration successful."));
  }

  @PostMapping("/login")
  public ResponseEntity<Dto> login(@RequestBody @Valid LoginAttemptDto loginAttemptDto) {

    String username = loginAttemptDto.getUsername();
    String password = loginAttemptDto.getPassword();
    Customer customer = customerService.getCustomerByUsername(username);

    if (customer == null) {
      return ResponseEntity.status(403)
          .body(new MessageDto(String.format("No such user with username: %s", username)));
    }
    if (!customerService.passwordIsCorrect(password, customer.getPassword())) {
      return ResponseEntity.status(403).body(new MessageDto("Password is not correct."));
    }

    User loggingUser = (User) customerService.loadCustomerByUsername(username);
    String token = customerService.getToken(loggingUser);
    return ResponseEntity.ok().body(new AccessTokenDto(token));
  }
}

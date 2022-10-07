package mzatka.bankappbackend.controllers;

import mzatka.bankappbackend.exceptions.CustomerAlreadyExistsException;
import mzatka.bankappbackend.exceptions.IncorrectPasswordException;
import mzatka.bankappbackend.exceptions.NoSuchCustomerException;
import mzatka.bankappbackend.models.dtos.*;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final CustomerService customerService;

  public AuthController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @PostMapping("/sign-up")
  public ResponseEntity<Dto> signUp(@RequestBody @Valid NewCustomerDto newCustomerDto) {
    if (customerService.customerExistsWithUsernameOrEmail(
        newCustomerDto.getUsername(), newCustomerDto.getEmail())) {
      throw new CustomerAlreadyExistsException("/auth/sign-up");
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
      throw new NoSuchCustomerException("/auth/login");
    }
    if (!customerService.passwordIsCorrect(password, customer.getPassword())) {
      throw new IncorrectPasswordException("/auth/login");
    }
    User loggingUser = (User) customerService.loadCustomerByUsername(username);
    String token = customerService.getToken(loggingUser);
    return ResponseEntity.ok().body(new AccessTokenDto(token));
  }
}

package mzatka.bankappbackend.controllers;

import lombok.RequiredArgsConstructor;
import mzatka.bankappbackend.events.OnRegistrationCompleteEvent;
import mzatka.bankappbackend.exceptions.CustomerAlreadyExistsException;
import mzatka.bankappbackend.exceptions.IncorrectPasswordException;
import mzatka.bankappbackend.exceptions.InvalidConfirmationTokenException;
import mzatka.bankappbackend.exceptions.NoSuchCustomerException;
import mzatka.bankappbackend.models.dtos.*;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.models.entities.VerificationToken;
import mzatka.bankappbackend.services.CustomerService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final CustomerService customerService;
  private final ApplicationEventPublisher applicationEventPublisher;

  @PostMapping("/sign-up")
  public ResponseEntity<Dto> signUp(
      @RequestBody @Valid NewCustomerDto newCustomerDto, HttpServletRequest request) {
    if (customerService.customerExistsWithUsernameOrEmail(
        newCustomerDto.getUsername(), newCustomerDto.getEmail())) {
      throw new CustomerAlreadyExistsException("/auth/sign-up");
    }
    Customer customer = customerService.registerNewCustomer(newCustomerDto);
    String url = request.getContextPath();
    applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(customer, url));
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

  @PostMapping("/confirmRegistration")
  public ResponseEntity<Dto> confirmRegistration(@RequestParam(name = "token") String token) {
    VerificationToken verificationToken = customerService.getVerificationToken(token);
    if (verificationToken == null || verificationToken.getCustomer() == null) {
      throw new InvalidConfirmationTokenException("/auth/confirmRegistration");
    }
    Customer customer = verificationToken.getCustomer();
    customer.setEnabled(true);
    customerService.saveCustomer(customer);
    return ResponseEntity.ok(new MessageDto("Customer registration confirmed successfully."));
  }
}
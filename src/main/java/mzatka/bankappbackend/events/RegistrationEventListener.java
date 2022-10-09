package mzatka.bankappbackend.events;

import lombok.RequiredArgsConstructor;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.services.CustomerService;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegistrationEventListener implements ApplicationListener<OnRegistrationCompleteEvent> {

  private final CustomerService customerService;
  private final JavaMailSender javaMailSender;

  @Override
  public void onApplicationEvent(@NotNull OnRegistrationCompleteEvent event) {
    this.confirmRegistration(event);
  }

  private void confirmRegistration(OnRegistrationCompleteEvent event) {
    Customer customer = event.getCustomer();
    String token = UUID.randomUUID().toString();
    customerService.createVerificationToken(customer, token);
    String recipientAddress = customer.getEmail();
    String subject = "Registration Confirmation";
    String confirmationUrl = "/auth/confirmRegistration?token=" + token;

    SimpleMailMessage email = new SimpleMailMessage();
    email.setFrom("bankapp.backend@seznam.cz");
    email.setTo(recipientAddress);
    email.setSubject(subject);
    email.setText("http://localhost:8080" + confirmationUrl);
    javaMailSender.send(email);
  }
}

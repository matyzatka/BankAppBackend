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
    this.sendTokenByMail(event);
  }

  private void sendTokenByMail(OnRegistrationCompleteEvent event) {
    Customer customer = event.getCustomer();
    String token = UUID.randomUUID().toString();
    customerService.createVerificationToken(customer, token);
    String recipientAddress = customer.getEmail();
    String subject = "Registration Confirmation";

    SimpleMailMessage email = new SimpleMailMessage();
    email.setFrom("bankapp.backend@azet.sk");
    email.setTo(recipientAddress);
    email.setSubject(subject);
    email.setText(
        "Hello, "
            + customer.getFirstName()
            + "!\n\n Please use this token for confirmation of your account by POST method (/auth/sendTokenByMail?token=[your_token]"
            + ("\n\n" + token + "\n\nThank you and have a nice day. :)"));
    javaMailSender.send(email);
  }
}

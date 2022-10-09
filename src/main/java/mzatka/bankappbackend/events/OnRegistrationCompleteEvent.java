package mzatka.bankappbackend.events;

import lombok.Getter;
import lombok.Setter;
import mzatka.bankappbackend.models.entities.Customer;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
  private String appUrl;
  private Customer customer;

  public OnRegistrationCompleteEvent(Customer customer, String appUrl) {
    super(customer);
    this.customer = customer;
    this.appUrl = appUrl;
  }
}

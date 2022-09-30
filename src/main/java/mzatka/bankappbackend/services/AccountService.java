package mzatka.bankappbackend.services;

import mzatka.bankappbackend.models.entities.Account;
import mzatka.bankappbackend.models.entities.Customer;

public interface AccountService {

  Account createNewAccount(Customer customer);
}

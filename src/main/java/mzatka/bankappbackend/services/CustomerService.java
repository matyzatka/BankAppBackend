package mzatka.bankappbackend.services;

import mzatka.bankappbackend.models.dtos.NewCustomerDto;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.models.entities.VerificationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomerService {

  Customer registerNewCustomer(NewCustomerDto newCustomerDto);

  void addRoleToCustomer(Customer customer, String roleName);

  Customer getCustomerByUsername(String username);

  Customer getCustomerById(Long id);

  void deleteCustomer(Long id);

  void assignValuesToCustomer(Customer customer, NewCustomerDto newCustomerDto);

  void saveCustomer(Customer customer);

  boolean customerExistsWithUsernameOrEmail(String username, String email);

  boolean passwordIsCorrect(String rawPassword, String encodedPassword);

  UserDetails loadCustomerByUsername(String username) throws UsernameNotFoundException;

  String getToken(UserDetails userDetails);

  Customer getCustomerFromAuthorizationHeader(String bearerToken);

  Customer getCustomerByVerificationToken(String verificationToken);

  VerificationToken getVerificationToken(String verificationToken);

  void createVerificationToken(Customer customer, String token);

    String getVerificationToken(Customer customer);
}

package mzatka.bankappbackend.services;

import mzatka.bankappbackend.models.dtos.NewCustomerDto;
import mzatka.bankappbackend.models.entities.Customer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomerService {

  void registerNewCustomer(NewCustomerDto newCustomerDto);

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
}

package mzatka.bankappbackend.services;

import mzatka.bankappbackend.models.dtos.NewCustomerDto;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.repositories.CustomerRepository;
import mzatka.bankappbackend.repositories.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerServiceImplTest {

  @Autowired CustomerService customerService;
  @Autowired CustomerRepository customerRepository;
  @Autowired RoleRepository roleRepository;
  @Autowired BCryptPasswordEncoder passwordEncoder;

  @Test
  public void adding_role_to_customer_can_throw_exception() {
    assertThrows(
        IllegalStateException.class,
        () -> customerService.addRoleToCustomer(new Customer(), "non-existing-role"));
  }

  @Test
  public void returns_customer_by_username() {
    String username = "bugKiller9876";
    customerService.saveCustomer(new Customer(username));
    assertNotNull(customerService.getCustomerByUsername(username));
  }

  @Test
  public void returns_customer_by_id() {
    Customer customer = new Customer();
    customerRepository.save(customer);
    assertNotNull(customerService.getCustomerById(1L));
  }

  @Test
  public void throws_Exception_when_customer_not_found() {
    assertThrows(IllegalStateException.class, () -> customerService.getCustomerById(99L));
  }

  @Test
  public void returns_True_if_userExists_byEmail_or_ByUsername() {
    customerService.saveCustomer(new Customer("bugKiller987", "bugKiller987@mail.com"));
    assertTrue(customerService.customerExistsWithUsernameOrEmail("bugKiller987", "some@mail.com"));
    assertTrue(
        customerService.customerExistsWithUsernameOrEmail("someName", "bugKiller987@mail.com"));
  }

  @Test
  public void returns_True_if_passwords_match() {
    String raw = "password";
    String encoded = passwordEncoder.encode(raw);
    assertTrue(customerService.passwordIsCorrect(raw, encoded));
  }

  @Test
  public void returns_UserDetails_from_username() {
    customerService.registerNewCustomer(
        new NewCustomerDto(
            "m.z@gmail.com",
            "newUser",
            "password",
            "Matouš",
            "Zátka",
            "22. 2. 1994",
            "604 778 881",
            "Praha, Oáza 27201"));
    assertNotNull(customerService.loadCustomerByUsername("newUser"));
  }

  @Test
  public void throws_Exception_if_user_not_found() {
    assertThrows(
        UsernameNotFoundException.class,
        () -> customerService.loadCustomerByUsername("nonExistingName"));
  }

  @Test
  public void returns_new_JWT_token() {
    customerService.registerNewCustomer(
        new NewCustomerDto(
            "m.zatka@gmail.com",
            "user",
            "password",
            "Matouš",
            "Zátka",
            "22. 2. 1994",
            "604 778 881",
            "Praha, Oáza 27201"));
    String token = customerService.getToken(customerService.loadCustomerByUsername("user"));
    assertNotNull(token);
    assertTrue(token.length() > 300);
  }

  @Test
  public void returns_customer_from_JWT_token() {
    customerService.registerNewCustomer(
        new NewCustomerDto(
            "m.zka@gmail.com",
            "UUser",
            "password",
            "Matouš",
            "Zátka",
            "22. 2. 1994",
            "604 778 881",
            "Praha, Oáza 27201"));
    String token =
        "Bearer " + customerService.getToken(customerService.loadCustomerByUsername("UUser"));
    Customer customer = customerService.getCustomerFromAuthorizationHeader(token);
    assertNotNull(customer);
    assertEquals("m.zka@gmail.com", customer.getEmail());
  }

  @Test
  public void throws_Exception_when_deleting_nonExisting_user() {
    assertThrows(Exception.class, () -> customerService.deleteCustomer(999L));
  }
}

package mzatka.bankappbackend.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import mzatka.bankappbackend.models.dtos.NewCustomerDto;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.models.entities.Role;
import mzatka.bankappbackend.models.entities.VerificationToken;
import mzatka.bankappbackend.repositories.CustomerRepository;
import mzatka.bankappbackend.repositories.RoleRepository;
import mzatka.bankappbackend.repositories.VerificationTokenRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

import static mzatka.bankappbackend.security.SecurityConfiguration.TOKEN_EXPIRATION_TIME;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  private final BCryptPasswordEncoder passwordEncoder;
  private final AccountService accountService;
  private final CustomerRepository customerRepository;
  private final RoleRepository roleRepository;
  private final VerificationTokenRepository verificationTokenRepository;

  @Override
  public Customer registerNewCustomer(NewCustomerDto newCustomerDto) {
    Customer customer = new Customer();
    assignValuesToCustomer(customer, newCustomerDto);
    customer.setAccount(accountService.createNewAccount(customer));
    addRoleToCustomer(customer, "ROLE_USER");
    customer.setEnabled(false);
    customerRepository.save(customer);
    return customer;
  }

  @Override
  public void addRoleToCustomer(Customer customer, String roleName) {
    Role role = roleRepository.findRoleByName(roleName);
    if (role == null) {
      throw new IllegalStateException(String.format("No such role: %s", roleName));
    }
    customer.getRoles().add(role);
    customerRepository.save(customer);
  }

  @Override
  public Customer getCustomerByUsername(String username) {
    return customerRepository.getCustomerByUsername(username);
  }

  @Override
  public Customer getCustomerById(Long id) {
    return customerRepository
        .findById(id)
        .orElseThrow(
            () ->
                new IllegalStateException(
                    String.format("Customer with id: %d not found in database.", id)));
  }

  @Override
  public void deleteCustomer(Long id) {
    if (customerRepository.findById(id).get() == null) {
      throw new IllegalStateException(
          String.format("Customer with id: %d not found in database.", id));
    }
    customerRepository.deleteCustomerById(id);
  }

  @Override
  public void assignValuesToCustomer(Customer customer, NewCustomerDto newCustomerDto) {
    customer.setUsername(newCustomerDto.getUsername());
    customer.setPassword(passwordEncoder.encode(newCustomerDto.getPassword()));
    customer.setFirstName(newCustomerDto.getFirstName());
    customer.setLastName(newCustomerDto.getLastName());
    customer.setDateOfBirth(newCustomerDto.getDateOfBirth());
    customer.setEmail(newCustomerDto.getEmail());
    customer.setPhone(newCustomerDto.getPhone());
    customer.setAddress(newCustomerDto.getAddress());
  }

  @Override
  public void saveCustomer(Customer customer) {
    customerRepository.save(customer);
  }

  @Override
  public boolean customerExistsWithUsernameOrEmail(String username, String email) {
    return customerRepository.existsCustomerByUsernameOrEmail(username, email);
  }

  @Override
  public boolean passwordIsCorrect(String rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }

  @Override
  public UserDetails loadCustomerByUsername(String username) throws UsernameNotFoundException {
    Customer customer = customerRepository.getCustomerByUsername(username);
    if (customer == null) {
      throw new UsernameNotFoundException("User not found in database.");
    }
    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    customer
        .getRoles()
        .forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
    return new org.springframework.security.core.userdetails.User(
        customer.getUsername(), customer.getPassword(), authorities);
  }

  @Override
  public String getToken(UserDetails userDetails) {
    Dotenv dotenv = Dotenv.load();
    Algorithm algorithm =
        Algorithm.HMAC512(
            Objects.requireNonNull(dotenv.get("JWT_SECRET_KEY")).getBytes(StandardCharsets.UTF_8));
    return JWT.create()
        .withSubject(userDetails.getUsername())
        .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
        .withIssuer(ServletUriComponentsBuilder.fromCurrentContextPath().path("/login").toString())
        .withClaim(
            "roles",
            userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()))
        .sign(algorithm);
  }

  @Override
  public Customer getCustomerFromAuthorizationHeader(String bearerToken) {
    Dotenv dotenv = Dotenv.load();
    String token = bearerToken.substring("bearer ".length());
    Algorithm algorithm =
        Algorithm.HMAC512(
            Objects.requireNonNull(dotenv.get("JWT_SECRET_KEY")).getBytes(StandardCharsets.UTF_8));
    JWTVerifier verifier = JWT.require(algorithm).build();
    DecodedJWT decodedJWT = verifier.verify(token);
    String username = decodedJWT.getSubject();
    return getCustomerByUsername(username);
  }

  @Override
  public Customer getCustomerByVerificationToken(String verificationToken) {
    return verificationTokenRepository
        .findVerificationTokenByToken(verificationToken)
        .getCustomer();
  }

  @Override
  public VerificationToken getVerificationToken(String verificationToken) {
    return verificationTokenRepository.findVerificationTokenByToken(verificationToken);
  }

  @Override
  public void createVerificationToken(Customer customer, String token) {
    VerificationToken verificationToken = new VerificationToken(token, customer);
    verificationTokenRepository.save(verificationToken);
  }

  @Override
  public String getVerificationToken(Customer customer) {
    return verificationTokenRepository
        .findVerificationTokenByCustomerUsername(customer.getUsername())
        .getToken();
  }
}

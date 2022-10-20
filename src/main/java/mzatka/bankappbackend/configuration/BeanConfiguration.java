package mzatka.bankappbackend.configuration;

import lombok.RequiredArgsConstructor;
import mzatka.bankappbackend.models.dtos.NewCustomerDto;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.models.entities.Role;
import mzatka.bankappbackend.models.enums.ProductType;
import mzatka.bankappbackend.repositories.RoleRepository;
import mzatka.bankappbackend.services.CustomerService;
import mzatka.bankappbackend.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.math.BigDecimal;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class BeanConfiguration {

  private final RoleRepository roleRepository;
  private final CustomerService customerService;
  private final ProductService productService;

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  public CommandLineRunner commandLineRunner() {
    return args -> {
      roleRepository.save(new Role("ROLE_USER"));
      roleRepository.save(new Role("ROLE_ADMIN"));

      customerService.registerNewCustomer(
          new NewCustomerDto(
              "admin.mail@gmail.com",
              "admin",
              "password",
              "Johnny",
              "Cash",
              "18-01-1990",
              "429-318-247",
              "Pleasant Hill, CA 3550"));
      Customer customer = customerService.getCustomerByUsername("admin");
      customer
          .getAccount()
          .getProducts()
          .forEach(
              product -> {
                product.setBalance(BigDecimal.valueOf(5000));
                productService.saveProduct(product);
              });
      productService.saveProduct(
          productService.createProduct(ProductType.SAVINGS_ACCOUNT, customer.getAccount()));
      customerService.addRoleToCustomer(customer, "ROLE_ADMIN");
      customer.setEnabled(true);
      customerService.saveCustomer(customer);
    };
  }
}

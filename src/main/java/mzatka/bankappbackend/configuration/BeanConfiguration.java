package mzatka.bankappbackend.configuration;

import mzatka.bankappbackend.models.dtos.NewCustomerDto;
import mzatka.bankappbackend.models.entities.Role;
import mzatka.bankappbackend.repositories.CustomerRepository;
import mzatka.bankappbackend.repositories.RoleRepository;
import mzatka.bankappbackend.services.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class BeanConfiguration {

  private final RoleRepository roleRepository;
  private final CustomerService customerService;
  private final CustomerRepository customerRepository;

  public BeanConfiguration(
      RoleRepository roleRepository,
      CustomerService customerService,
      CustomerRepository customerRepository) {
    this.roleRepository = roleRepository;
    this.customerService = customerService;
    this.customerRepository = customerRepository;
  }

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
              "user.admin@gmail.com",
              "admin",
              "password",
              "Admin",
              "von Admin",
              "22. 2. 1994",
              "312 777 888",
              "Prague, Center 5000, 27201"));

      customerService.addRoleToCustomer(
          customerRepository.getCustomerByUsername("admin"), "ROLE_ADMIN");
    };
  }
}

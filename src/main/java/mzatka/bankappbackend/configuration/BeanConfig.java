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
public class BeanConfig {

  private final RoleRepository roleRepository;
  private final CustomerService customerService;
  private final CustomerRepository customerRepository;

  public BeanConfig(
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
              "matous.zatka@gmail.com",
              "matyzatka",
              "password",
              "Matouš",
              "Zátka",
              "22. 2. 1994",
              "604 778 881",
              "Kladno, Oáza 27201"));

      customerService.addRoleToCustomer(
          customerRepository.getCustomerByUsername("matyzatka"), "ROLE_ADMIN");

      customerService.registerNewCustomer(
          new NewCustomerDto(
              "adamnguyenvan@gmail.com",
              "adamnguyen",
              "password",
              "Adam",
              "Nguyen",
              "1. 8. 1998",
              "608 255 461",
              "Rimavská Sobota, 33504"));

      customerService.registerNewCustomer(
          new NewCustomerDto(
              "matous.zatka@gmail.com",
              "qreitos",
              "password",
              "Peter",
              "Špišák",
              "13. 2. 1992",
              "605 748 133",
              "Detva, Slovakia, 44468"));
    };
  }
}

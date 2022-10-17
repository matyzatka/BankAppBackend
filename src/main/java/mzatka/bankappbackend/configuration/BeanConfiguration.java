package mzatka.bankappbackend.configuration;

import lombok.RequiredArgsConstructor;
import mzatka.bankappbackend.models.dtos.NewCustomerDto;
import mzatka.bankappbackend.models.entities.Role;
import mzatka.bankappbackend.repositories.RoleRepository;
import mzatka.bankappbackend.services.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class BeanConfiguration {

  private final RoleRepository roleRepository;
  private final CustomerService customerService;

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
              "1.1.1999",
              "77777777",
              "Kladno, Oaza, 999"));
      customerService.addRoleToCustomer(
          customerService.getCustomerByUsername("matyzatka"), "ROLE_ADMIN");
    };
  }
}

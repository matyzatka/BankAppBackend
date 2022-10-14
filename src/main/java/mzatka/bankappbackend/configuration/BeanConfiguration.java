package mzatka.bankappbackend.configuration;

import lombok.RequiredArgsConstructor;
import mzatka.bankappbackend.repositories.CustomerRepository;
import mzatka.bankappbackend.repositories.RoleRepository;
import mzatka.bankappbackend.services.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class BeanConfiguration {

  private final RoleRepository roleRepository;
  private final CustomerService customerService;
  private final CustomerRepository customerRepository;

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}

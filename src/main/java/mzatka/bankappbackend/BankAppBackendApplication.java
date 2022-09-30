package mzatka.bankappbackend;

import mzatka.bankappbackend.models.entities.Role;
import mzatka.bankappbackend.repositories.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BankAppBackendApplication {

  private final RoleRepository roleRepository;

  public BankAppBackendApplication(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  public static void main(String[] args) {
    SpringApplication.run(BankAppBackendApplication.class, args);
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
    };
  }
}

package mzatka.bankappbackend.models.dtos;

import lombok.Data;
import mzatka.bankappbackend.models.entities.Account;
import mzatka.bankappbackend.models.entities.Role;

import java.util.List;

@Data
public class CustomerDto implements Dto {

  private Long id;
  private String username;
  private String firstName;
  private String lastName;
  private String dateOfBirth;
  private String email;
  private String phone;
  private String address;
  private Account account;
  private List<Role> roles;
}

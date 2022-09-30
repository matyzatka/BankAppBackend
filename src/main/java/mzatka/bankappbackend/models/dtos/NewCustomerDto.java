package mzatka.bankappbackend.models.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class NewCustomerDto implements Dto {

  @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE)
  @NotBlank
  @NotNull
  private String email;

  @NotBlank @NotNull private String username;
  @NotBlank @NotNull private String password;
  @NotBlank @NotNull private String firstName;
  @NotBlank @NotNull private String lastName;
  @NotBlank @NotNull private String dateOfBirth;
  @NotBlank @NotNull private String phone;
  @NotBlank @NotNull private String address;
}

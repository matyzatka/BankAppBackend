package mzatka.bankappbackend.models.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LoginAttemptDto implements Dto {

  @NotBlank @NotNull private String username;

  @NotBlank @NotNull private String password;
}

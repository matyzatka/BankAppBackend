package mzatka.bankappbackend.models.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ConfirmationPasswordDto {

  @NotNull @NotBlank private String password;
}

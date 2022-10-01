package mzatka.bankappbackend.models.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DeleteConfirmationDto implements Dto {

  @NotNull @NotBlank private String password;
  @NotNull @NotBlank private String iban;
}

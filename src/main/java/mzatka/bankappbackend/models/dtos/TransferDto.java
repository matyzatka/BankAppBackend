package mzatka.bankappbackend.models.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TransferDto {

  @NotBlank @NotNull private String iban;
  @NotNull private Integer amount;
}

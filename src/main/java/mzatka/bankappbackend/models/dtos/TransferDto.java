package mzatka.bankappbackend.models.dtos;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TransferDto implements Dto {

  @NotBlank @NotNull private String iban;

  @NotNull
  @Min(1)
  private Integer amount;
}

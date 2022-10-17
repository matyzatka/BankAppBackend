package mzatka.bankappbackend.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class TransferDto implements Dto {

  @NotBlank @NotNull private String iban;
  @NotBlank @NotNull private String pinCode;

  @NotNull
  @Min(1)
  private Integer amount;
}

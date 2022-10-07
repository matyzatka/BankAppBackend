package mzatka.bankappbackend.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class TransactionDto implements Dto {

  @NotNull @NotBlank private String sendingIban;
  @NotNull @NotBlank private String receivingIban;
  @NotNull private Double amount;


}

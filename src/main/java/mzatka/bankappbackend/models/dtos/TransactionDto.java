package mzatka.bankappbackend.models.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TransactionDto implements Dto {

  @NotNull @NotBlank private String sendingIban;
  @NotNull @NotBlank private String receivingIban;
  @NotNull private Double amount;
}

package mzatka.bankappbackend.models.dtos;

import lombok.Data;

@Data
public class AddProductDto implements Dto {

  private String productName;
  private String password;
}

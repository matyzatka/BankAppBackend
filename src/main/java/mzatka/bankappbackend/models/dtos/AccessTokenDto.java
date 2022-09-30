package mzatka.bankappbackend.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessTokenDto implements Dto {

  private String accessToken;
}

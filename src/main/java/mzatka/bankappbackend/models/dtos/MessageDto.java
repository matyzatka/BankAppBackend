package mzatka.bankappbackend.models.dtos;

import lombok.Data;

@Data
public class MessageDto implements Dto {

  private String message;

  public MessageDto(String message) {
    this.message = message;
  }
}

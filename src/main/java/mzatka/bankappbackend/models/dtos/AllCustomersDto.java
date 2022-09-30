package mzatka.bankappbackend.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AllCustomersDto implements Dto {

  private List<CustomerDto> customers;
}

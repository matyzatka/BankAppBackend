package mzatka.bankappbackend.services;

import mzatka.bankappbackend.models.dtos.AllCustomersDto;
import mzatka.bankappbackend.models.dtos.CustomerDto;
import mzatka.bankappbackend.models.entities.Customer;

public interface DtoService {
  CustomerDto convertToDto(Customer customer);

  AllCustomersDto showAllCustomers();
}

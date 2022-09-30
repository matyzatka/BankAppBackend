package mzatka.bankappbackend.services;

import mzatka.bankappbackend.models.dtos.AllCustomersDto;
import mzatka.bankappbackend.models.dtos.CustomerDto;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.repositories.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

@Service
public class DtoServiceImpl implements DtoService {

  private final ModelMapper modelMapper;
  private final CustomerRepository customerRepository;

  public DtoServiceImpl(ModelMapper modelMapper, CustomerRepository customerRepository) {
    this.modelMapper = modelMapper;
    this.customerRepository = customerRepository;
  }

  @Override
  public CustomerDto convertToDto(Customer customer) {
    return modelMapper.map(customer, CustomerDto.class);
  }

  @Override
  public AllCustomersDto showAllCustomers() {
    return new AllCustomersDto(
        customerRepository.findAll().stream().map(this::convertToDto).collect(toList()));
  }
}

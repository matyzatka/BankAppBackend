package mzatka.bankappbackend.services;

import lombok.RequiredArgsConstructor;
import mzatka.bankappbackend.models.dtos.AllCustomersDto;
import mzatka.bankappbackend.models.dtos.CustomerDto;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.repositories.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class DtoServiceImpl implements DtoService {

  private final ModelMapper modelMapper;
  private final CustomerRepository customerRepository;
  private final RetrofitService retrofitService;

  @Override
  public CustomerDto convertToDto(Customer customer) {
    return modelMapper.map(customer, CustomerDto.class);
  }

  @Override
  public CustomerDto convertToDto(Customer customer, String currency) {
    CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
    customerDto
        .getAccount()
        .getProducts()
        .forEach(
            product -> {
              try {
                product.setBalance(
                    product.getBalance().multiply(retrofitService.getCurrency(currency)));
                product.setCurrency(currency.toUpperCase());
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            });
    return customerDto;
  }

  @Override
  public AllCustomersDto showAllCustomers() {
    return new AllCustomersDto(
        customerRepository.findAll().stream().map(this::convertToDto).collect(toList()));
  }
}

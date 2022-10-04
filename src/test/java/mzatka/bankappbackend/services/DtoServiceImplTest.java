package mzatka.bankappbackend.services;

import mzatka.bankappbackend.models.dtos.AllCustomersDto;
import mzatka.bankappbackend.models.dtos.CustomerDto;
import mzatka.bankappbackend.models.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DtoServiceImplTest {

  @Autowired DtoService dtoService;
  @Autowired CustomerService customerService;

  @Test
  public void returns_CustomerDTO() {
    Customer customer = new Customer("DTO");
    CustomerDto customerDto = dtoService.convertToDto(customer);
    assertNotNull(customerDto);
    assertEquals(customer.getUsername(), customerDto.getUsername());
  }

  @Test
  public void returns_nonEmpty_allCustomersDTO() {
    customerService.saveCustomer(new Customer("Another DTO"));
    AllCustomersDto allCustomersDto = dtoService.showAllCustomers();
    assertNotNull(allCustomersDto);
  }
}

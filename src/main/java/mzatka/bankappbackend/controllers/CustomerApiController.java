package mzatka.bankappbackend.controllers;

import mzatka.bankappbackend.models.dtos.Dto;
import mzatka.bankappbackend.models.dtos.MessageDto;
import mzatka.bankappbackend.models.dtos.NewCustomerDto;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.services.CustomerService;
import mzatka.bankappbackend.services.DtoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/customer")
public class CustomerApiController {

  private final DtoService dtoService;
  private final CustomerService customerService;

  public CustomerApiController(DtoService dtoService, CustomerService customerService) {
    this.dtoService = dtoService;
    this.customerService = customerService;
  }

  @GetMapping("/all")
  public ResponseEntity<Dto> getAllCustomers() {
    return ResponseEntity.ok(dtoService.showAllCustomers());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Customer> getCustomerById(@PathVariable(name = "id") Long id) {
    return ResponseEntity.ok(customerService.getCustomerById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Dto> updateCustomer(
      @PathVariable(name = "id") Long id, @RequestBody @Valid NewCustomerDto customerDto) {
    Customer customer = customerService.getCustomerById(id);
    customerService.assignValuesToCustomer(customer, customerDto);
    customerService.saveCustomer(customer);
    return ResponseEntity.ok(new MessageDto("Customer updated successfully."));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Dto> deleteCustomerById(@PathVariable(name = "id") Long id) {
    try {
      customerService.deleteCustomer(id);
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      return ResponseEntity.badRequest()
          .body(new MessageDto(String.format("No customer found with id: %d.", id)));
    }
  }
}

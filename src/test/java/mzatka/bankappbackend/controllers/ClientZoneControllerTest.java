package mzatka.bankappbackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import mzatka.bankappbackend.models.dtos.NewCustomerDto;
import mzatka.bankappbackend.repositories.CustomerRepository;
import mzatka.bankappbackend.services.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ClientZoneControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private CustomerService customerService;
  @Autowired private CustomerRepository customerRepository;
  @Autowired private ObjectMapper objectMapper;

  @BeforeEach
  public void init() {
    customerService.registerNewCustomer(
        new NewCustomerDto(
            "testuser@mail.com",
            "client",
            "password",
            "first",
            "name",
            "1.1.1999",
            "77777777",
            "address"));
  }

  @Test
  public void can_show_customer() throws Exception {
    String token =
        "Bearer " + customerService.getToken(customerService.loadCustomerByUsername("client"));
    mockMvc.perform(get("/client-zone").header("Authorization", token)).andExpect(status().isOk());
  }

  @AfterEach
  public void cleanup() {
    customerRepository.deleteAll();
  }
}

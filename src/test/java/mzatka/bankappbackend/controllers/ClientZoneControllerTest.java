package mzatka.bankappbackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import mzatka.bankappbackend.exceptions.InsufficientFundsException;
import mzatka.bankappbackend.exceptions.NoSuchProductWithIbanException;
import mzatka.bankappbackend.exceptions.UnauthorizedAccountUsageException;
import mzatka.bankappbackend.models.dtos.NewCustomerDto;
import mzatka.bankappbackend.models.dtos.TransactionDto;
import mzatka.bankappbackend.models.entities.Customer;
import mzatka.bankappbackend.models.enums.ProductType;
import mzatka.bankappbackend.repositories.CustomerRepository;
import mzatka.bankappbackend.services.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

  @Test
  public void can_make_transaction() throws Exception {
    customerService.registerNewCustomer(
        new NewCustomerDto(
            "another@mail.com",
            "anotherUser",
            "password",
            "first",
            "name",
            "1.1.1999",
            "77777777",
            "address"));
    String receiveIban = "50000000/4005";
    Customer receiver = customerService.getCustomerByUsername("anotherUser");
    receiver.getAccount().getProducts().stream()
        .filter(product -> product.getProductType().equals(ProductType.CHECKING_ACCOUNT))
        .forEach(product -> product.setIBAN(receiveIban));

    String sendIban = "1000000/9000";
    Customer sender = customerService.getCustomerByUsername("client");
    sender.getAccount().getProducts().stream()
        .filter(product -> product.getProductType().equals(ProductType.CHECKING_ACCOUNT))
        .forEach(
            product -> {
              product.setIBAN(sendIban);
              product.setBalance(BigDecimal.valueOf(1000));
            });
    String token =
        "Bearer " + customerService.getToken(customerService.loadCustomerByUsername("client"));
    mockMvc
        .perform(
            post("/client-zone/pay")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        new TransactionDto(sendIban, receiveIban, 500.0))))
        .andExpect(status().isOk());

    mockMvc
        .perform(
            post("/client-zone/pay")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        new TransactionDto(sendIban, receiveIban, 1000.0))))
        .andExpect(status().isBadRequest())
        .andExpect(
            result ->
                assertTrue(result.getResolvedException() instanceof InsufficientFundsException));

    mockMvc
        .perform(
            post("/client-zone/pay")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        new TransactionDto(receiveIban, sendIban, 1000.0))))
        .andExpect(status().isUnauthorized())
        .andExpect(
            result ->
                assertTrue(
                    result.getResolvedException() instanceof UnauthorizedAccountUsageException));

    mockMvc
        .perform(
            post("/client-zone/pay")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        new TransactionDto("16546431213", receiveIban, 1000.0))))
        .andExpect(status().isBadRequest())
        .andExpect(
            result ->
                assertTrue(
                    result.getResolvedException() instanceof NoSuchProductWithIbanException));

    mockMvc
        .perform(
            post("/client-zone/pay")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void can_update_customer() throws Exception {
    String token =
        "Bearer " + customerService.getToken(customerService.loadCustomerByUsername("client"));
    mockMvc
        .perform(
            put("/client-zone/update")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        new NewCustomerDto(
                            "something@mail.com",
                            "updated",
                            "password",
                            "first",
                            "name",
                            "1.1.1999",
                            "77777777",
                            "address"))))
        .andExpect(status().isOk());
  }

  @AfterEach
  public void cleanup() {
    customerRepository.deleteAll();
  }
}

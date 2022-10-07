package mzatka.bankappbackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import mzatka.bankappbackend.models.dtos.NewCustomerDto;
import mzatka.bankappbackend.models.dtos.TransferDto;
import mzatka.bankappbackend.repositories.ProductRepository;
import mzatka.bankappbackend.services.CustomerService;
import mzatka.bankappbackend.services.ProductService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AtmControllerTest {

  @Autowired private CustomerService customerService;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private ProductRepository productRepository;

  @Test
  public void returns_correct_response_after_deposit() throws Exception {
    customerService.registerNewCustomer(
        new NewCustomerDto(
            "new@mail.com",
            "newDepositor",
            "password",
            "new",
            "depositor",
            "1. 1. 2000",
            "322 122 332",
            "Unknown"));
    String token =
        "Bearer "
            + customerService.getToken(customerService.loadCustomerByUsername("newDepositor"));
    String iban =
        Objects.requireNonNull(
                productRepository.findAll().stream()
                    .filter(
                        product ->
                            product.getAccount().getCustomer().getUsername().equals("newDepositor"))
                    .findFirst()
                    .orElse(null))
            .getIBAN();

    mockMvc
        .perform(
            post("/atm/deposit")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new TransferDto(iban, 500))))
        .andExpect(status().isOk())
        .andDo(print());

    iban = "invalid IBAN";

    mockMvc
        .perform(
            post("/atm/deposit")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(new TransferDto("non_existing_iban", 500))))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", Is.is("IBAN does not exist.")))
        .andDo(print());

    mockMvc
            .perform(
                    post("/atm/withdraw")
                            .header("Authorization", token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    objectMapper.writeValueAsString(new TransferDto(iban, 500))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", Is.is("IBAN does not exist.")))
            .andDo(print());
  }
}

package mzatka.bankappbackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import mzatka.bankappbackend.models.dtos.LoginAttemptDto;
import mzatka.bankappbackend.models.dtos.NewCustomerDto;
import mzatka.bankappbackend.repositories.CustomerRepository;
import mzatka.bankappbackend.services.CustomerService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private CustomerService customerService;
  @Autowired private CustomerRepository customerRepository;

  @BeforeEach
  public void init() {
    NewCustomerDto newCustomerDto =
        new NewCustomerDto(
            "testuser@mail.com",
            "client",
            "password",
            "first",
            "name",
            "1.1.1999",
            "77777777",
            "address");
    customerService.registerNewCustomer(newCustomerDto);
  }

  @Test
  public void can_save_new_user() throws Exception {
    NewCustomerDto newCustomerDto =
        new NewCustomerDto(
            "maill@mail.com",
            "user",
            "password",
            "first",
            "name",
            "1.1.1999",
            "77777777",
            "address");
    mockMvc
        .perform(
            post("/auth/sign-up")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCustomerDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message", Is.is("Registration successful.")))
        .andDo(print());
  }

  @Test
  public void can_login_user() throws Exception {
    mockMvc
        .perform(
            post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(new LoginAttemptDto("client", "password"))))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @AfterEach
  public void cleanup() {
    customerRepository.deleteAll();
  }
}
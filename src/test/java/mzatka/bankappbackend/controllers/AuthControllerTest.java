package mzatka.bankappbackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import mzatka.bankappbackend.exceptions.CustomerAlreadyExistsException;
import mzatka.bankappbackend.exceptions.IncorrectPasswordException;
import mzatka.bankappbackend.exceptions.InvalidConfirmationTokenException;
import mzatka.bankappbackend.exceptions.NoSuchCustomerException;
import mzatka.bankappbackend.models.dtos.LoginAttemptDto;
import mzatka.bankappbackend.models.dtos.NewCustomerDto;
import mzatka.bankappbackend.models.entities.Customer;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
  public void throws_exception_if_user_exist() throws Exception {
    mockMvc
        .perform(
            post("/auth/sign-up")
                .contentType(APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        new NewCustomerDto(
                            "testuser@mail.com",
                            "client",
                            "password",
                            "first",
                            "name",
                            "1.1.1999",
                            "77777777",
                            "address"))))
        .andExpect(
            result ->
                assertTrue(result.getResolvedException() instanceof CustomerAlreadyExistsException))
        .andDo(print());
  }

  @Test
  public void returns_ok_if_user_doesnt_exist() throws Exception {
    mockMvc
        .perform(
            post("/auth/sign-up")
                .contentType(APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        new NewCustomerDto(
                            "anothertestuser@mail.com",
                            "another_client",
                            "password",
                            "first",
                            "name",
                            "1.1.1999",
                            "77777777",
                            "address"))))
        .andExpect(status().isOk())
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

  @Test
  public void throws_exception_if_user_doesnt_exists_during_login() throws Exception {
    mockMvc
        .perform(
            post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        new LoginAttemptDto("nonexistingusername", "password"))))
        .andExpect(
            result -> assertTrue(result.getResolvedException() instanceof NoSuchCustomerException))
        .andDo(print());
  }

  @Test
  public void throws_exception_if_password_not_correct_during_login() throws Exception {
    mockMvc
        .perform(
            post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        new LoginAttemptDto("client", "invalid_password"))))
        .andExpect(
            result ->
                assertTrue(result.getResolvedException() instanceof IncorrectPasswordException))
        .andDo(print());
  }

  @Test
  public void throws_exception_if_token_not_valid() throws Exception {
    mockMvc
        .perform(
            post("/auth/confirmRegistration?token=" + "invalidTokenValue")
                .contentType(APPLICATION_JSON))
        .andExpect(
            result ->
                assertTrue(
                    result.getResolvedException() instanceof InvalidConfirmationTokenException))
        .andDo(print());
  }

  @Test
  public void returns_ok_if_token_valid() throws Exception {
    mockMvc
        .perform(
            post("/auth/sign-up")
                .contentType(APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        new NewCustomerDto(
                            "newMail@mail.com",
                            "newTest",
                            "password",
                            "first",
                            "name",
                            "1.1.1999",
                            "77777777",
                            "address"))))
        .andExpect(status().isOk())
        .andDo(print());

    Customer customer = customerService.getCustomerByUsername("newTest");
    String token = customerService.getVerificationToken(customer);
    mockMvc
        .perform(post("/auth/confirmRegistration?token=" + token).contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @AfterEach
  public void cleanup() {
    customerRepository.deleteAll();
  }
}

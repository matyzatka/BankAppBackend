package mzatka.bankappbackend.security;

import mzatka.bankappbackend.models.dtos.NewCustomerDto;
import mzatka.bankappbackend.services.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CustomAuthorizationFilterTest {

  @Autowired CustomerService customerService;

  @Test
  public void custom_filter_test() throws ServletException, IOException {
    customerService.registerNewCustomer(
        new NewCustomerDto(
            "adamnguyenvan@gmail.com",
            "testuser",
            "password",
            "test",
            "User",
            "1. 1. 1998",
            "677 255 471",
            "Omaha Sobota, 33504"));
    String bearerToken =
        "Bearer " + customerService.getToken(customerService.loadCustomerByUsername("testuser"));
    HttpServletRequest servletRequest = mock(HttpServletRequest.class);
    HttpServletResponse servletResponse = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);
    CustomAuthorizationFilter customAuthorizationFilter = new CustomAuthorizationFilter();
    Assertions.assertThrows(
        Exception.class,
        () -> customAuthorizationFilter.doFilter(servletRequest, servletResponse, filterChain));
    when(servletRequest.getHeader(AUTHORIZATION)).thenReturn(bearerToken);
    customAuthorizationFilter.doFilter(servletRequest, servletResponse, filterChain);
    verify(filterChain).doFilter(servletRequest, servletResponse);
  }
}

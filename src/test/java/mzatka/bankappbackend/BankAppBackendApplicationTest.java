package mzatka.bankappbackend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BankAppBackendApplicationTest {

  @Autowired ApplicationContext context;

  @Test
  public void testContextLoads() {
    assertNotNull(context);
  }
}

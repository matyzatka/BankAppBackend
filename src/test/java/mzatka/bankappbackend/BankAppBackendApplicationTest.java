package mzatka.bankappbackend;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class BankAppBackendApplicationTest {

    @Autowired
    ApplicationContext context;

    @Test
    public void testContextLoads() {
        assertNotNull(context);
    }
}

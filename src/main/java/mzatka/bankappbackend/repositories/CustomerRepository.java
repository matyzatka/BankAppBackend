package mzatka.bankappbackend.repositories;

import mzatka.bankappbackend.models.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
  boolean existsCustomerByUsernameOrEmail(String username, String email);

  Customer getCustomerByUsername(String username);

  @Modifying
  @Query("DELETE FROM Customer c WHERE c.id=:id")
  void deleteCustomerById(Long id);
}

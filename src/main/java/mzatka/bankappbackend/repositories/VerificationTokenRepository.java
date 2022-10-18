package mzatka.bankappbackend.repositories;

import mzatka.bankappbackend.models.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
  VerificationToken findVerificationTokenByToken(String token);

  VerificationToken findVerificationTokenByCustomerUsername(String username);
}

package mzatka.bankappbackend.repositories;

import mzatka.bankappbackend.models.entities.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {}

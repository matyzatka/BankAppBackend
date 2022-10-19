package mzatka.bankappbackend.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import mzatka.bankappbackend.models.entities.TransactionLog;

import java.util.List;

@Data
@AllArgsConstructor
public class AllTransactionLogsDto implements Dto {

  private List<TransactionLog> transactionHistory;
}

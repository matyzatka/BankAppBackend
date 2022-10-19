package mzatka.bankappbackend.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String senderName;
  private String senderIban;
  private String receiverName;
  private String receiverIban;
  private String amount;
  private String datetime;

  public TransactionLog(
      String senderName,
      String senderIban,
      String receiverName,
      String receiverIban,
      String amount) {
    this.senderName = senderName;
    this.senderIban = senderIban;
    this.receiverName = receiverName;
    this.receiverIban = receiverIban;
    this.amount = amount;
    LocalDateTime timeNow = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    this.datetime = timeNow.format(formatter);
  }
}

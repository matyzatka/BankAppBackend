package mzatka.bankappbackend.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Account {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @OneToOne(cascade = ALL)
  @JsonBackReference
  private Customer customer;

  @OneToMany private List<Product> products;

  private String createdAt;
  private Boolean isBlocked;

  public Account(Customer customer) {
    this.customer = customer;
    this.products = new ArrayList<>();
    LocalDateTime timeNow = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    this.createdAt = timeNow.format(formatter);
    this.isBlocked = false;
  }
}

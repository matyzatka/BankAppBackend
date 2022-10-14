package mzatka.bankappbackend.models.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;
import static org.hibernate.annotations.CascadeType.ALL;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "customer")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Customer {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private String username;
  @JsonIgnore private String password;
  private String firstName;
  private String lastName;
  private String dateOfBirth;
  private String email;
  private String phone;
  private String address;

  @OneToOne(cascade = CascadeType.ALL)
  @Cascade(ALL)
  private Account account;

  private boolean enabled;

  @ManyToMany(fetch = FetchType.EAGER)
  private List<Role> roles = new ArrayList<>();

  public Customer(String username) {
    this.username = username;
  }

  public Customer(String username, String email) {
    this.username = username;
    this.email = email;
  }
}

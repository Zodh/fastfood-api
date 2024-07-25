package br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.activation;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.CustomerEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activation_code")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivationCodeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID key;
  @ManyToOne
  private CustomerEntity customer;
  @Column(name = "expiration")
  private LocalDateTime expiration;

}

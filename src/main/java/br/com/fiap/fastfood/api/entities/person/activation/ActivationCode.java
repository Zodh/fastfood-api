package br.com.fiap.fastfood.api.entities.person.activation;

import br.com.fiap.fastfood.api.entities.person.Customer;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.Data;

@Data
public class ActivationCode {

  private UUID key;
  private final Customer customer;
  private final LocalDateTime expiration;

  public ActivationCode(Customer customer, LocalDateTime expiration) {
    this.customer = customer;
    this.expiration = expiration;
  }

  public boolean isValid() {
    return Objects.nonNull(customer)
        && !this.customer.isActive()
        && this.expiration.isAfter(LocalDateTime.now());
  }

}

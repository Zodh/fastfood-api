package br.com.fiap.fastfood.api.core.domain.model.person.activation;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
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
    if (Objects.isNull(customer) || customer.isActive()) {
      ErrorDetail errorDetail = new ErrorDetail("person", "A pessoa não pode ser nula e deve estar inativa!");
      throw new DomainException(errorDetail);
    }
  }
}

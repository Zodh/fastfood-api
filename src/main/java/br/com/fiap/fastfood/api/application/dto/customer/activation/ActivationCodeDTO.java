package br.com.fiap.fastfood.api.core.application.dto.customer.activation;

import br.com.fiap.fastfood.api.core.application.dto.customer.CustomerDTO;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivationCodeDTO {

  private UUID key;
  private final CustomerDTO customer;
  private final LocalDateTime expiration;

}

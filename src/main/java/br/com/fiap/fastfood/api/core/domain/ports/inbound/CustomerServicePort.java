package br.com.fiap.fastfood.api.core.domain.ports.inbound;

import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import java.util.UUID;

public interface CustomerServicePort {

  void register(Customer customer);

  void activate(UUID code);

  Customer identify(String documentNumber);

  void resendVerificationLink(String email);

}

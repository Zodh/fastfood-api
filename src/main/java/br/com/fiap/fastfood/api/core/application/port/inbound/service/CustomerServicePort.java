package br.com.fiap.fastfood.api.core.application.port.inbound.service;

import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.person.vo.Document;
import java.util.UUID;

public interface CustomerServicePort {

  void register(Customer customer);
  void activate(UUID code);
  Customer identify(String documentNumber);
  void resendActivationCode(String email);
  Customer getById(Long id);
  Customer getByDocument(Document document);

}

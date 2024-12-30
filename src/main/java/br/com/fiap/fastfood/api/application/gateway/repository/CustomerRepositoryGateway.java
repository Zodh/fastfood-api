package br.com.fiap.fastfood.api.application.gateway.repository;

import br.com.fiap.fastfood.api.application.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.application.dto.customer.DocumentTypeEnum;
import java.util.Optional;

public interface CustomerRepositoryGateway extends BaseRepositoryGateway<CustomerDTO, Long> {

  Optional<CustomerDTO> findByEmail(String email);

  Optional<CustomerDTO> findByDocument(String documentNumber, DocumentTypeEnum documentType);

  void activate(Long identifier);

}

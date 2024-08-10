package br.com.fiap.fastfood.api.core.application.port.repository;

import br.com.fiap.fastfood.api.core.application.dto.customer.CustomerDTO;
import br.com.fiap.fastfood.api.core.application.dto.customer.DocumentTypeEnum;
import br.com.fiap.fastfood.api.core.application.port.BaseRepository;
import java.util.Optional;

public interface CustomerRepositoryPort extends BaseRepository<CustomerDTO, Long> {

  Optional<CustomerDTO> findByEmail(String email);

  Optional<CustomerDTO> findByDocument(String documentNumber, DocumentTypeEnum documentType);

  void activate(Long identifier);

}

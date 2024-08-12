package br.com.fiap.fastfood.api.core.application.port.repository;

import br.com.fiap.fastfood.api.core.application.dto.customer.activation.ActivationCodeDTO;
import br.com.fiap.fastfood.api.core.application.port.BaseRepository;
import java.util.UUID;

public interface ActivationCodeRepositoryPort extends BaseRepository<ActivationCodeDTO, UUID> {

  void deleteAllActivationCodeByCustomer(Long customerId);

}

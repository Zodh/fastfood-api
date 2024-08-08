package br.com.fiap.fastfood.api.core.application.port.repository;

import br.com.fiap.fastfood.api.core.application.port.BaseRepository;
import br.com.fiap.fastfood.api.core.domain.model.person.activation.ActivationCode;
import java.util.UUID;

public interface ActivationCodeRepositoryPort extends BaseRepository<ActivationCode, UUID> {

  void deleteAllActivationCodeByCustomer(Long customerId);

}

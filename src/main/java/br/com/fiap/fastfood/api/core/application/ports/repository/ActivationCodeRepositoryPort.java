package br.com.fiap.fastfood.api.core.application.ports.repository;

import br.com.fiap.fastfood.api.core.application.ports.BaseRepository;
import br.com.fiap.fastfood.api.core.domain.model.person.activation.ActivationCode;
import java.util.UUID;

public interface ActivationCodeRepositoryPort extends BaseRepository<ActivationCode, UUID> {

  void deleteAllActivationCodeByCustomer(Long customerId);

}

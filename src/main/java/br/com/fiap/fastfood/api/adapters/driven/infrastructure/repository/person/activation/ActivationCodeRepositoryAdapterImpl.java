package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.person.activation;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.person.activation.ActivationCodeEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.ActivationCodeMapper;
import br.com.fiap.fastfood.api.core.domain.model.person.activation.ActivationCode;
import br.com.fiap.fastfood.api.core.application.port.repository.ActivationCodeRepositoryPort;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActivationCodeRepositoryAdapterImpl implements ActivationCodeRepositoryPort {

  private final ActivationCodeRepository repository;
  private final ActivationCodeMapper mapper;

  @Override
  public Optional<ActivationCode> findById(UUID identifier) {
    Optional<ActivationCodeEntity> activationCodeOpt = repository.findById(identifier);
    return activationCodeOpt.map(mapper::toDomain);
  }

  @Override
  public ActivationCode save(ActivationCode data) {
    ActivationCodeEntity entity = mapper.toEntity(data);
    ActivationCodeEntity persistedEntity = repository.save(entity);
    return mapper.toDomain(persistedEntity);
  }

  @Override
  public boolean delete(UUID identifier) {
    Optional<ActivationCodeEntity> activationCodeOpt = repository.findById(identifier);
    if (activationCodeOpt.isEmpty()) {
      return false;
    }
    repository.deleteById(identifier);
    return true;
  }

  @Override
  public void deleteAllActivationCodeByCustomer(Long customerId) {
    repository.deleteAllByCustomerId(customerId);
  }
}

package br.com.fiap.fastfood.api.adapters.gateway;

import br.com.fiap.fastfood.api.application.dto.customer.activation.ActivationCodeDTO;
import br.com.fiap.fastfood.api.infrastructure.dao.entity.person.activation.ActivationCodeEntity;
import br.com.fiap.fastfood.api.adapters.mapper.ActivationCodeMapperInfra;
import br.com.fiap.fastfood.api.infrastructure.dao.repository.person.activation.ActivationCodeRepository;
import br.com.fiap.fastfood.api.application.gateway.repository.ActivationCodeRepositoryGateway;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActivationCodeRepositoryAdapterImpl implements ActivationCodeRepositoryGateway {

  private final ActivationCodeRepository repository;
  private final ActivationCodeMapperInfra mapper;

  @Override
  public Optional<ActivationCodeDTO> findById(UUID identifier) {
    Optional<ActivationCodeEntity> activationCodeOpt = repository.findById(identifier);
    return activationCodeOpt.map(mapper::toDTO);
  }

  @Override
  public ActivationCodeDTO save(ActivationCodeDTO data) {
    ActivationCodeEntity entity = mapper.toEntity(data);
    ActivationCodeEntity persistedEntity = repository.save(entity);
    return mapper.toDTO(persistedEntity);
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

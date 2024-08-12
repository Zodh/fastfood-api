package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.followup.FollowUpEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.FollowUpMapperInfra;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.follow.up.FollowUpRepository;
import br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpDTO;
import br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpStateEnum;
import br.com.fiap.fastfood.api.core.application.port.repository.FollowUpRepositoryPort;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FollowUpRepositoryAdapterImpl implements FollowUpRepositoryPort {

  private final FollowUpRepository repository;
  private final FollowUpMapperInfra mapper;

  @Autowired
  public FollowUpRepositoryAdapterImpl(FollowUpRepository repository, FollowUpMapperInfra mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Optional<FollowUpDTO> findById(Long identifier) {
    Optional<FollowUpEntity> optEntity = repository.findById(identifier);
    return optEntity.map(mapper::toDTO);
  }

  @Override
  public FollowUpDTO save(FollowUpDTO data) {
    FollowUpEntity entity = mapper.toEntity(data);
    FollowUpEntity persistedEntity = repository.save(entity);
    return mapper.toDTO(persistedEntity);
  }

  @Override
  public boolean delete(Long identifier) {
    repository.deleteById(identifier);
    return true;
  }

  @Override
  public List<FollowUpDTO> findAllByState(FollowUpStateEnum state) {
    return repository.findAllByState(
        br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.followup.FollowUpStateEnum.valueOf(state.name())).stream().map(mapper::toDTO).collect(
        Collectors.toList());
  }

  @Override
  public Optional<FollowUpDTO> findByOrderId(Long orderId) {
    return repository.findByOrder_Id(orderId).map(mapper::toDTO);
  }

  @Override
  public void saveAll(List<FollowUpDTO> updated) {
    repository.saveAll(updated.stream().map(mapper::toEntity).toList());
  }

  @Override
  public List<FollowUpDTO> findAll() {
    return Optional.ofNullable(repository.findAllLazy()).orElse(Collections.emptyList()).stream().map(mapper::toDTO).collect(Collectors.toList());
  }

}

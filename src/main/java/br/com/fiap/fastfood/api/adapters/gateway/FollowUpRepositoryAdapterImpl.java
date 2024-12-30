package br.com.fiap.fastfood.api.adapters.gateway;

import br.com.fiap.fastfood.api.application.dto.followup.FollowUpDTO;
import br.com.fiap.fastfood.api.application.dto.followup.FollowUpStateEnum;
import br.com.fiap.fastfood.api.infrastructure.dao.entity.followup.FollowUpEntity;
import br.com.fiap.fastfood.api.adapters.mapper.FollowUpMapperInfra;
import br.com.fiap.fastfood.api.infrastructure.dao.repository.follow.up.FollowUpRepository;
import br.com.fiap.fastfood.api.application.gateway.repository.FollowUpRepositoryGateway;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FollowUpRepositoryAdapterImpl implements FollowUpRepositoryGateway {

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
        br.com.fiap.fastfood.api.infrastructure.dao.entity.followup.FollowUpStateEnum.valueOf(state.name())).stream().map(mapper::toDTO).collect(
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

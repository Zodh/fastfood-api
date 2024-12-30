package br.com.fiap.fastfood.api.application.gateway.repository;

import br.com.fiap.fastfood.api.application.dto.followup.FollowUpDTO;
import br.com.fiap.fastfood.api.application.dto.followup.FollowUpStateEnum;
import java.util.List;
import java.util.Optional;

public interface FollowUpRepositoryGateway extends BaseRepositoryGateway<FollowUpDTO, Long> {

  List<FollowUpDTO> findAllByState(FollowUpStateEnum state);
  Optional<FollowUpDTO> findByOrderId(Long orderId);
  void saveAll(List<FollowUpDTO> updated);
  List<FollowUpDTO> findAll();

}

package br.com.fiap.fastfood.api.core.application.port.repository;

import br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpDTO;
import br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpStateEnum;
import br.com.fiap.fastfood.api.core.application.port.BaseRepository;
import java.util.List;
import java.util.Optional;

public interface FollowUpRepositoryPort extends BaseRepository<FollowUpDTO, Long> {

  List<FollowUpDTO> findAllByState(FollowUpStateEnum state);
  Optional<FollowUpDTO> findByOrderId(Long orderId);
  void saveAll(List<FollowUpDTO> updated);
  List<FollowUpDTO> findAll();

}

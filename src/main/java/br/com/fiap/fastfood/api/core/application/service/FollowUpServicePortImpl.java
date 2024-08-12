package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpDTO;
import br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpResponseDTO;
import br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpStateEnum;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.FollowUpServicePort;
import br.com.fiap.fastfood.api.core.application.port.repository.FollowUpRepositoryPort;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FollowUpServicePortImpl implements FollowUpServicePort {

  private final FollowUpRepositoryPort followUpRepositoryPort;
  private final List<FollowUpStateEnum> requiredStates = List.of(FollowUpStateEnum.RECEIVED, FollowUpStateEnum.IN_PREPARATION, FollowUpStateEnum.READY);

  public FollowUpServicePortImpl(FollowUpRepositoryPort followUpRepositoryPort) {
    this.followUpRepositoryPort = followUpRepositoryPort;
  }

  @Override
  public List<FollowUpResponseDTO> findAll() {
    List<FollowUpDTO> allFollowUp = followUpRepositoryPort.findAll();
    Map<FollowUpStateEnum, List<FollowUpDTO>> followUpByState = allFollowUp.stream().collect(
        Collectors.groupingBy(FollowUpDTO::getState));
    requiredStates.forEach(s -> {
      if (!followUpByState.containsKey(s)) {
        followUpByState.put(s, List.of());
      }
    });
    return followUpByState.entrySet().stream().map(entry -> new FollowUpResponseDTO(entry.getKey(), entry.getValue().stream().sorted(
        Comparator.comparing(FollowUpDTO::getShowOrder)).collect(Collectors.toList()))).collect(Collectors.toList());
  }
}

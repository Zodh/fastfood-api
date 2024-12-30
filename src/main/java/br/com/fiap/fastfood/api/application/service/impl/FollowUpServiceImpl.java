package br.com.fiap.fastfood.api.application.service.impl;

import br.com.fiap.fastfood.api.application.dto.followup.FollowUpDTO;
import br.com.fiap.fastfood.api.application.dto.followup.FollowUpResponseDTO;
import br.com.fiap.fastfood.api.application.dto.followup.FollowUpStateEnum;
import br.com.fiap.fastfood.api.application.service.FollowUpService;
import br.com.fiap.fastfood.api.application.gateway.repository.FollowUpRepositoryGateway;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FollowUpServiceImpl implements FollowUpService {

  private final FollowUpRepositoryGateway followUpRepositoryGateway;
  private final List<FollowUpStateEnum> requiredStates = List.of(FollowUpStateEnum.RECEIVED, FollowUpStateEnum.IN_PREPARATION, FollowUpStateEnum.READY);

  public FollowUpServiceImpl(FollowUpRepositoryGateway followUpRepositoryGateway) {
    this.followUpRepositoryGateway = followUpRepositoryGateway;
  }

  @Override
  public List<FollowUpResponseDTO> findAll() {
    List<FollowUpDTO> allFollowUp = followUpRepositoryGateway.findAll();
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

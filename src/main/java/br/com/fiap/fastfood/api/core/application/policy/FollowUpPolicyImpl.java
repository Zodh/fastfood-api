package br.com.fiap.fastfood.api.core.application.policy;

import br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpDTO;
import br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpStateEnum;
import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.application.mapper.FollowUpMapperApp;
import br.com.fiap.fastfood.api.core.application.mapper.FollowUpMapperAppImpl;
import br.com.fiap.fastfood.api.core.application.port.repository.FollowUpRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.aggregate.FollowUpAggregate;
import br.com.fiap.fastfood.api.core.domain.model.followup.FollowUp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class FollowUpPolicyImpl implements FollowUpPolicy {

  private final FollowUpRepositoryPort repositoryPort;
  private final FollowUpMapperApp mapperApp;

  public FollowUpPolicyImpl(FollowUpRepositoryPort repositoryPort) {
    this.repositoryPort = repositoryPort;
    this.mapperApp = new FollowUpMapperAppImpl();
  }

  @Override
  public void updateOrderInFollowUp(OrderDTO order, FollowUpStateEnum nextState) {
    FollowUpDTO followUpDTO = repositoryPort.findByOrderId(order.getId()).orElse(null);
    if (Objects.isNull(followUpDTO)) {
      followUpDTO = new FollowUpDTO(null, 0, order, nextState);
    }
    List<FollowUp> allFollowUpInPreviousState = getAllFollowUpInPreviousState(followUpDTO);
    FollowUp followUp = mapperApp.toDomain(followUpDTO);
    followUp.setState(mapperApp.mapState(nextState, followUp));
    FollowUpAggregate followUpAggregate = new FollowUpAggregate(followUp);

    if (!nextState.equals(FollowUpStateEnum.FINISHED)) {
      List<FollowUp> allFollowUpInSameState = repositoryPort.findAllByState(nextState).stream()
          .map(f -> {
            FollowUp fu = mapperApp.toDomain(f);
            fu.setState(mapperApp.mapState(f.getState(), fu));
            return fu;
          }).collect(Collectors.toList());
      followUpAggregate.updateFollowUp(allFollowUpInPreviousState, allFollowUpInSameState);
      save(allFollowUpInSameState);
    } else {
      followUpAggregate.removeFollowUp(allFollowUpInPreviousState, followUp);
      repositoryPort.delete(followUp.getId());
    }
    if (!allFollowUpInPreviousState.isEmpty()) {
      save(allFollowUpInPreviousState);
    }
  }


  private void save(List<FollowUp> domainList) {
    List<FollowUpDTO> dtoList = Optional.ofNullable(domainList).orElse(Collections.emptyList()).stream().map(mapperApp::toDTO).collect(Collectors.toList());
    repositoryPort.saveAll(dtoList);
  }

  private @NotNull List<FollowUp> getAllFollowUpInPreviousState(FollowUpDTO followUpDTO) {
    return Objects.isNull(followUpDTO.getId()) && followUpDTO.getState().equals(FollowUpStateEnum.RECEIVED)
        ? new ArrayList<>()
        : repositoryPort.findAllByState(followUpDTO.getState()).stream().map(f -> {
      FollowUp fu = mapperApp.toDomain(f);
      fu.setState(mapperApp.mapState(f.getState(), fu));
      return fu;
    }).collect(Collectors.toList());
  }

}

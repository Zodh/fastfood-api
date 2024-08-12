package br.com.fiap.fastfood.api.core.application.mapper;

import br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpDTO;
import br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpStateEnum;
import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.application.exception.ApplicationException;
import br.com.fiap.fastfood.api.core.domain.model.followup.FollowUp;
import br.com.fiap.fastfood.api.core.domain.model.followup.state.FollowUpState;
import br.com.fiap.fastfood.api.core.domain.model.followup.state.impl.OrderFinishedFollowUpState;
import br.com.fiap.fastfood.api.core.domain.model.followup.state.impl.OrderInPreparationFollowUpState;
import br.com.fiap.fastfood.api.core.domain.model.followup.state.impl.OrderReadyFollowUpState;
import br.com.fiap.fastfood.api.core.domain.model.followup.state.impl.OrderReceivedFollowUpState;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface FollowUpMapperApp {

  @Mapping(target = "state", ignore = true)
  @Mapping(source = "dto.order", target = "order", qualifiedByName = "mapOrder")
  FollowUp toDomain(FollowUpDTO dto);

  @Named("mapOrder")
  default Order mapOrder(OrderDTO dto) {
    OrderMapperApp orderMapperApp = new OrderMapperAppImpl();
    Order order = orderMapperApp.toDomain(dto);
    order.changeState(orderMapperApp.mapStateImpl(dto.getState(), order));
    return order;
  }

  @Mapping(source = "domain.state", target = "state", qualifiedByName = "mapStateEnum")
  @Mapping(source = "domain.order", target = "order", qualifiedByName = "mapOrderDTO")
  FollowUpDTO toDTO(FollowUp domain);

  @Named("mapOrderDTO")
  default OrderDTO mapOrderDTO(Order order) {
    OrderMapperApp orderMapperApp = new OrderMapperAppImpl();
    return orderMapperApp.toDTO(order);
  }

  @Named("mapStateEnum")
  default FollowUpStateEnum mapStateEnum(FollowUpState followUpState) {
    if (followUpState instanceof OrderReceivedFollowUpState) {
      return FollowUpStateEnum.RECEIVED;
    }
    if (followUpState instanceof OrderInPreparationFollowUpState) {
      return FollowUpStateEnum.IN_PREPARATION;
    }
    if (followUpState instanceof OrderReadyFollowUpState) {
      return FollowUpStateEnum.READY;
    }
    if (followUpState instanceof OrderFinishedFollowUpState) {
      return FollowUpStateEnum.FINISHED;
    }
    throw new ApplicationException("Não foi encontrado um estado aproriado para o acompanhamento.", "Invalid follow up state");
  }

  default FollowUpState mapState(FollowUpStateEnum followUpStateEnum, FollowUp followUp) {
    if (followUpStateEnum.equals(FollowUpStateEnum.RECEIVED)) {
      return new OrderReceivedFollowUpState(followUp);
    }
    if (followUpStateEnum.equals(FollowUpStateEnum.IN_PREPARATION)) {
      return new OrderInPreparationFollowUpState(followUp);
    }
    if (followUpStateEnum.equals(FollowUpStateEnum.READY)) {
      return new OrderReadyFollowUpState(followUp);
    }
    if (followUpStateEnum.equals(FollowUpStateEnum.FINISHED)) {
      return new OrderFinishedFollowUpState(followUp);
    }
    throw new ApplicationException("Não foi encontrado um estado aproriado para o acompanhamento.", "Invalid follow up state");
  }

}

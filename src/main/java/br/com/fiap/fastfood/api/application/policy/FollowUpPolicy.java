package br.com.fiap.fastfood.api.application.policy;

import br.com.fiap.fastfood.api.application.dto.followup.FollowUpStateEnum;
import br.com.fiap.fastfood.api.application.dto.order.OrderDTO;

public interface FollowUpPolicy {

  void updateOrderInFollowUp(OrderDTO order, FollowUpStateEnum nextState);

}

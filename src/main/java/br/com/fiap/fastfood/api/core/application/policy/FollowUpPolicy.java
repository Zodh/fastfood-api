package br.com.fiap.fastfood.api.core.application.policy;

import br.com.fiap.fastfood.api.core.application.dto.followup.FollowUpStateEnum;
import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;

public interface FollowUpPolicy {

  void updateOrderInFollowUp(OrderDTO order, FollowUpStateEnum nextState);

}

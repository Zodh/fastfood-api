package br.com.fiap.fastfood.api.core.application.port.inbound.policy;

import br.com.fiap.fastfood.api.core.domain.model.order.Order;

public interface FollowUpPolicyPort {

  void updateFollowUpView(Order order);

}

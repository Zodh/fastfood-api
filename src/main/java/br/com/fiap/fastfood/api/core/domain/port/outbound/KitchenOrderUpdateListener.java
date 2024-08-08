package br.com.fiap.fastfood.api.core.domain.port.outbound;

import br.com.fiap.fastfood.api.core.domain.model.order.Order;

public interface KitchenOrderUpdateListener {

  void updateFollowUpView(Order order);

}

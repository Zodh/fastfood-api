package br.com.fiap.fastfood.api.core.domain.ports.outbound;

import br.com.fiap.fastfood.api.core.domain.model.order.Order;

public interface KitchenOrderUpdateListener {

  void updateFollowUpView(Order order);

}

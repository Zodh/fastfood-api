package br.com.fiap.fastfood.api.core.application.event.follow.up;

import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.ports.outbound.KitchenOrderUpdateListener;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class FollowUpEventListener implements ApplicationListener<FollowUpEvent>,
    KitchenOrderUpdateListener {

  @Override
  public void onApplicationEvent(FollowUpEvent event) {
    updateFollowUpView(event.getOrder());
  }

  @Override
  public void updateFollowUpView(Order order) {
    // TODO: Send order update to connected devices. Criado em: 07/08/2024 ás 09:28:33.
    System.out.println("Agora o estado do pedido é: " + order.getState().getDescription());
  }
}

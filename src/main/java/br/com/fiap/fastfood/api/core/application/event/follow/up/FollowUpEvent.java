package br.com.fiap.fastfood.api.core.application.event.follow.up;

import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FollowUpEvent extends ApplicationEvent {

  private Order order;

  public FollowUpEvent(Object source, Order order) {
    super(source);
    this.order = order;
  }

}

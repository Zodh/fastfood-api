package br.com.fiap.fastfood.api.core.application.event.payment;

import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;

@Getter
public class PaymentEvent extends ApplicationEvent {

  private final Order order;
  private final PaymentOperationEnum operation;


  public PaymentEvent(Object source, @NonNull Order order, PaymentOperationEnum operation) {
    super(source);
    this.order = order;
    this.operation = operation;
  }
}

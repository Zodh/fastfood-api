package br.com.fiap.fastfood.api.core.application.event.payment;

import br.com.fiap.fastfood.api.core.domain.ports.inbound.InvoiceServicePort;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.OrderServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener implements ApplicationListener<PaymentEvent> {

  private final InvoiceServicePort invoiceServicePort;
  private final OrderServicePort orderServicePort;

  @Autowired
  public PaymentEventListener(InvoiceServicePort invoiceServicePort, OrderServicePort orderServicePort) {
    this.invoiceServicePort = invoiceServicePort;
    this.orderServicePort = orderServicePort;
  }

  @Override
  public void onApplicationEvent(PaymentEvent event) {
    //In future, implement something like factory + strategy if necessary.
    if (event.getOperation().equals(PaymentOperationEnum.GENERATE)) {
      invoiceServicePort.create(event.getOrder());
    }
    if (event.getOperation().equals(PaymentOperationEnum.CANCEL)) {
        invoiceServicePort.cancelAllInvoicesRelatedToOrder(event.getOrder());
    }
    if (event.getOperation().equals(PaymentOperationEnum.PAY)) {
      orderServicePort.turnReadyToPrepare(event.getOrder().getId());
    }
  }

}

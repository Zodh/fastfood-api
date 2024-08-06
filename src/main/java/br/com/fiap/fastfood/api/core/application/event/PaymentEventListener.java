package br.com.fiap.fastfood.api.core.application.event;

import br.com.fiap.fastfood.api.core.domain.ports.inbound.InvoiceServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener implements ApplicationListener<PaymentEvent> {

  private final InvoiceServicePort invoiceServicePort;

  @Autowired
  public PaymentEventListener(InvoiceServicePort invoiceServicePort) {
    this.invoiceServicePort = invoiceServicePort;
  }

  @Override
  public void onApplicationEvent(PaymentEvent event) {
    if (event.getOperation().equals(PaymentOperationEnum.GENERATE)) {
      invoiceServicePort.create(event.getOrder());
    }
    if (event.getOperation().equals(PaymentOperationEnum.CANCEL)) {
        invoiceServicePort.cancelAllInvoicesRelatedToOrder(event.getOrder());
    }
  }

}

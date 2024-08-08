package br.com.fiap.fastfood.api.core.application.port.inbound.policy;

import br.com.fiap.fastfood.api.core.domain.model.order.Order;

public interface OrderInvoicePolicyPort {

  void cancelInvoiceByOrder(Order order);
  void generateInvoiceByOrder(Order order);
  void payOrderInvoice(Order order);

}

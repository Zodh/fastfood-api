package br.com.fiap.fastfood.api.core.application.policy;

import br.com.fiap.fastfood.api.core.domain.model.order.Order;

public interface OrderInvoicePolicy {

  void cancelInvoiceByOrder(Order order);
  void generateInvoiceByOrder(Order order);
  void payOrderInvoice(Order order);

}

package br.com.fiap.fastfood.api.core.application.policy;

import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;

public interface OrderInvoicePolicy {

  void cancelOrderInvoice(OrderDTO order);
  void generateOrderInvoice(OrderDTO order);
  void defineOrderEligibleToPreparation(OrderDTO order);

}

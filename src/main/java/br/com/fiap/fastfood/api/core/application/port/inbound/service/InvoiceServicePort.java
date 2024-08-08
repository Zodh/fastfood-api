package br.com.fiap.fastfood.api.core.application.port.inbound.service;

import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import java.util.List;

public interface InvoiceServicePort {

    Invoice executeFakeCheckout(Order order);
    List<Invoice> getInvoicesByOrder(Long orderId);

}

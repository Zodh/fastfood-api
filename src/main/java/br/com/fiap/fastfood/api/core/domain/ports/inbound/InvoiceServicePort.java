package br.com.fiap.fastfood.api.core.domain.ports.inbound;

import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;

public interface InvoiceServicePort {

    Invoice create(Order order);
    Invoice pay(Long orderId, Long invoiceId);
    Invoice cancel(Long id);

}

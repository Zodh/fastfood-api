package br.com.fiap.fastfood.api.core.domain.ports.inbound;

import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;

public interface InvoiceServicePort {

    Invoice create(Order order);
    Invoice executeFakeCheckout(Long orderId);
    Invoice cancel(Long id);

}

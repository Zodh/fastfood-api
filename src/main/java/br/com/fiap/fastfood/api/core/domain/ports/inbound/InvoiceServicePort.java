package br.com.fiap.fastfood.api.core.domain.ports.inbound;

import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import java.util.List;
import org.springframework.lang.NonNull;

public interface InvoiceServicePort {

    Invoice create(Order order);
    Invoice executeFakeCheckout(Order order);
    void cancelAllInvoicesRelatedToOrder(@NonNull Order order);

    List<Invoice> getInvoicesByOrder(Long orderId);
}

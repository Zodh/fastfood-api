package br.com.fiap.fastfood.api.core.domain.aggregate;

import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.InvoicePendingState;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvoiceAggregate {

    public Invoice createInvoice(Order order) {
        Invoice invoice = new Invoice();
        invoice.changeState(new InvoicePendingState(invoice));
        invoice.setPrice(order.getPrice());
        invoice.setExternalInvoiceId(null);
        invoice.setVendor(null);
        invoice.setOrder(order);
        invoice.setCreated(LocalDateTime.now());
        return invoice;
    }
}

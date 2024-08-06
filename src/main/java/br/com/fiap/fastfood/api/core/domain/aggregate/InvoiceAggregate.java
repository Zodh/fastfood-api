package br.com.fiap.fastfood.api.core.domain.aggregate;

import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.impl.InvoicePendingState;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceAggregate {

    private Invoice invoice;

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

    public Invoice pay() {
        Invoice invoice = this.invoice;
        invoice.getState().payInvoice();
        return invoice;
    }
}

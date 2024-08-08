package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.application.policy.OrderInvoicePolicy;
import br.com.fiap.fastfood.api.core.application.port.repository.InvoiceRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.aggregate.InvoiceAggregate;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.InvoiceServicePort;
import java.util.List;

public class InvoiceServicePortImpl implements InvoiceServicePort {

    private final InvoiceRepositoryPort repository;
    private final OrderInvoicePolicy orderInvoicePolicy;

    public InvoiceServicePortImpl(InvoiceRepositoryPort repository, OrderInvoicePolicy orderInvoicePolicy) {
        this.repository = repository;
        this.orderInvoicePolicy = orderInvoicePolicy;
    }

    @Override
    public Invoice executeFakeCheckout(Order order) {
        InvoiceAggregate invoiceAggregate = new InvoiceAggregate(order.getInvoice());
        Invoice invoice = invoiceAggregate.pay();
        Invoice result = repository.save(invoice);
        orderInvoicePolicy.payOrderInvoice(order);
        return result;
    }

    @Override
    public List<Invoice> getInvoicesByOrder(Long orderId) {
        return repository.findByOrderId(orderId);
    }

}

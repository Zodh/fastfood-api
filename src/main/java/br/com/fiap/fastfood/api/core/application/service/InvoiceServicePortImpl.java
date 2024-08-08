package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.application.port.inbound.policy.OrderInvoicePolicyPort;
import br.com.fiap.fastfood.api.core.application.port.repository.InvoiceRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.aggregate.InvoiceAggregate;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.InvoiceServicePort;
import java.util.List;

public class InvoiceServicePortImpl implements InvoiceServicePort {

    private final InvoiceRepositoryPort repository;
    private final OrderInvoicePolicyPort orderInvoicePolicyPort;

    public InvoiceServicePortImpl(InvoiceRepositoryPort repository, OrderInvoicePolicyPort orderInvoicePolicyPort) {
        this.repository = repository;
        this.orderInvoicePolicyPort = orderInvoicePolicyPort;
    }

    @Override
    public Invoice executeFakeCheckout(Order order) {
        InvoiceAggregate invoiceAggregate = new InvoiceAggregate(order.getInvoice());
        Invoice invoice = invoiceAggregate.pay();
        Invoice result = repository.save(invoice);
        orderInvoicePolicyPort.payOrderInvoice(order);
        return result;
    }

    @Override
    public List<Invoice> getInvoicesByOrder(Long orderId) {
        return repository.findByOrderId(orderId);
    }

}

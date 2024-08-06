package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.application.ports.repository.InvoiceRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.aggregate.InvoiceAggregate;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.InvoiceServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServicePortImpl implements InvoiceServicePort {

    private final InvoiceRepositoryPort repository;
    private final OrderServicePortImpl orderServicePort;

    @Autowired
    public InvoiceServicePortImpl(InvoiceRepositoryPort repository, OrderServicePortImpl orderServicePort) {
        this.repository = repository;
        this.orderServicePort = orderServicePort;
    }

    @Override
    public Invoice create(Order order) {
        InvoiceAggregate invoiceAggregate = new InvoiceAggregate();
        Invoice invoice = invoiceAggregate.createInvoice(order);

        return repository.save(invoice);
    }

    @Override
    public Invoice executeFakeCheckout(Long orderId) {
        Order order = orderServicePort.getById(orderId);
        InvoiceAggregate invoiceAggregate = new InvoiceAggregate(order.getInvoice());
        Invoice invoice = invoiceAggregate.pay();
        return repository.save(invoice);
    }

    @Override
    public Invoice cancel(Long invoiceId) {
        Invoice invoice = repository.findById(invoiceId)
                .orElseThrow(() -> new NotFoundException("O id do pagamento não foi encontrado!"));

        invoice.cancel();
        return repository.save(invoice);
    }

    @Scheduled(fixedDelay = 30000)
    public void expireOldInvoices() {
        repository.expireOldInvoices();
    }
}

package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.application.ports.repository.InvoiceRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.aggregate.InvoiceAggregate;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.InvoiceServicePort;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServicePortImpl implements InvoiceServicePort {

    private final InvoiceRepositoryPort repository;
    @Value("${invoice.expiration.time}")
    private int invoiceExpirationTime;

    @Autowired
    public InvoiceServicePortImpl(InvoiceRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Invoice create(Order order) {
        InvoiceAggregate invoiceAggregate = new InvoiceAggregate();
        cancelAllInvoicesRelatedToOrder(order);
        Invoice invoice = invoiceAggregate.createInvoice(order);
        return repository.save(invoice);
    }

    @Override
    public Invoice executeFakeCheckout(Order order) {
        InvoiceAggregate invoiceAggregate = new InvoiceAggregate(order.getInvoice());
        Invoice invoice = invoiceAggregate.pay();
        return repository.save(invoice);
    }

    @Override
    public void cancelAllInvoicesRelatedToOrder(@NonNull Order order) {
        if (Objects.nonNull(order.getId()) && order.getId() > 0 && order.hasInvoice()) {
            repository.cancelPendingInvoicesByOrder(order.getId());
        }
    }

    @Override
    public List<Invoice> getInvoicesByOrder(Long orderId) {

        return repository.findByOrderId(orderId);
    }


    @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.SECONDS)
    public void expireOldInvoices() {
        repository.expireOldInvoices(invoiceExpirationTime);
    }

}

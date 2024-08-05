package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice.InvoiceStatus;
import br.com.fiap.fastfood.api.core.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.application.ports.repository.InvoiceRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.aggregate.InvoiceAggregate;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.InvoiceCancelledState;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.InvoiceExpiredState;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.InvoicePaidState;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.InvoiceServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    public Invoice pay(Long orderId, Long invoiceId) {
        Order order = orderServicePort.getById(orderId);

        // Verifica se existe o invoiceId informado dentro do pedido
        List<Invoice> currentInvoices = order.getInvoices();
        // TODO: Poderia jogar essa função dentro do dominio de Invoice?
        Optional<Invoice> invoiceToPay = Optional.ofNullable(currentInvoices).orElse(Collections.emptyList()).stream()
                .filter(current -> current.getId().equals(invoiceId))
                .findFirst();

        if (invoiceToPay.isPresent() && !invoiceToPay.get().isExpired()) {
            invoiceToPay.get().changeState(new InvoicePaidState(invoiceToPay.get()));

            return repository.save(invoiceToPay.get());
        } else {
            throw new NotFoundException("O id do pagamento não existe para o pedido informado!");
        }
    }

    @Override
    public Invoice cancel(Long invoiceId) {
        Optional<Invoice> invoice = repository.findById(invoiceId);

        if (invoice.isEmpty()) {
            throw new NotFoundException("O id do pagamento não existe");
        }

        invoice.ifPresent(val -> {
                    val.changeState(new InvoiceCancelledState(invoice.get()));
                    repository.save(invoice.get());
                });

        return invoice.get();
    }

    @Scheduled(fixedDelay = 30000)
    public void verifyExpiredOrder() {
        List<Invoice> pendingInvoices = repository.fetchInvoicesByStatus(InvoiceStatus.PENDING.name());

        pendingInvoices.forEach(invoice -> {
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(invoice.getCreated(), now);
            if (duration.toMinutes() > 5) {
                invoice.changeState(new InvoiceExpiredState(invoice));
                repository.save(invoice);
            }
        });

    }
}

package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.application.mapper.InvoiceMapperApp;
import br.com.fiap.fastfood.api.core.application.mapper.InvoiceMapperAppImpl;
import br.com.fiap.fastfood.api.core.application.mapper.OrderMapperApp;
import br.com.fiap.fastfood.api.core.application.mapper.OrderMapperAppImpl;
import br.com.fiap.fastfood.api.core.application.policy.OrderInvoicePolicy;
import br.com.fiap.fastfood.api.core.application.port.repository.InvoiceRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.aggregate.InvoiceAggregate;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.application.port.inbound.service.InvoiceServicePort;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import java.util.List;

public class InvoiceServicePortImpl implements InvoiceServicePort {

    private final InvoiceRepositoryPort repository;
    private final OrderInvoicePolicy orderInvoicePolicy;
    private final InvoiceMapperApp invoiceMapperApp;
    private final OrderMapperApp orderMapperApp;

    public InvoiceServicePortImpl(InvoiceRepositoryPort repository, OrderInvoicePolicy orderInvoicePolicy) {
        this.repository = repository;
        this.orderInvoicePolicy = orderInvoicePolicy;
        this.invoiceMapperApp = new InvoiceMapperAppImpl();
        this.orderMapperApp = new OrderMapperAppImpl();
    }

    @Override
    public InvoiceDTO executeFakeCheckout(OrderDTO orderDTO) {
        Order order = orderMapperApp.toDomain(orderDTO);
        Invoice invoice = order.getInvoice();

        InvoiceAggregate invoiceAggregate = new InvoiceAggregate(invoice);
        invoiceAggregate.pay();

        InvoiceDTO paidInvoice = invoiceMapperApp.toDto(invoice);
        InvoiceDTO result = repository.save(paidInvoice);

        orderInvoicePolicy.defineOrderEligibleToPreparation(orderDTO);
        return result;
    }

    @Override
    public List<InvoiceDTO> getInvoicesByOrder(Long orderId) {
        return repository.findByOrderId(orderId);
    }

}

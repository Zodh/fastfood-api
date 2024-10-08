package br.com.fiap.fastfood.api.application.usecase.impl;

import br.com.fiap.fastfood.api.application.usecase.InvoiceUseCase;
import br.com.fiap.fastfood.api.core.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.application.gateway.mapper.InvoiceMapperApp;
import br.com.fiap.fastfood.api.application.gateway.mapper.OrderMapperApp;
import br.com.fiap.fastfood.api.application.policy.OrderInvoicePolicy;
import br.com.fiap.fastfood.api.application.gateway.repository.InvoiceRepositoryGateway;
import br.com.fiap.fastfood.api.entities.invoice.Invoice;
import br.com.fiap.fastfood.api.entities.order.Order;

import java.util.List;

public class InvoiceUseCaseImpl implements
    InvoiceUseCase {

    private final InvoiceRepositoryGateway repository;
    private final OrderInvoicePolicy orderInvoicePolicy;
    private final InvoiceMapperApp invoiceMapperApp;
    private final OrderMapperApp orderMapperApp;

    public InvoiceUseCaseImpl(InvoiceRepositoryGateway repository, OrderInvoicePolicy orderInvoicePolicy, InvoiceMapperApp invoiceMapperApp, OrderMapperApp orderMapperApp) {
        this.repository = repository;
        this.orderInvoicePolicy = orderInvoicePolicy;
        this.invoiceMapperApp = invoiceMapperApp;
        this.orderMapperApp = orderMapperApp;
    }

    @Override
    public InvoiceDTO executeFakeCheckout(OrderDTO orderDTO) {
        Order order = orderMapperApp.toDomain(orderDTO);
        Invoice invoice = order.getInvoice();
        invoice.getState().payInvoice();

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

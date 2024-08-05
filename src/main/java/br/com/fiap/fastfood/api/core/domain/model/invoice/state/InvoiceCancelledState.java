package br.com.fiap.fastfood.api.core.domain.model.invoice.state;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice.InvoiceStatus;
import br.com.fiap.fastfood.api.core.domain.exception.InvoiceStateException;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.invoice.InvoiceState;

public class InvoiceCancelledState extends InvoiceState {
    public InvoiceCancelledState(Invoice invoice) {
        super(invoice);
    }

    @Override
    protected void payInvoice() {
        throw new InvoiceStateException(
                String.format("Não é possível pagar uma fatura com status '%s'.", getDescription()));
    }

    @Override
    protected void paidInvoice() {
        throw new InvoiceStateException(
                String.format("Não é possível a fatura estar paga com status '%s'.", getDescription()));
    }

    @Override
    protected void cancelInvoice() {
        throw new InvoiceStateException(
                String.format("Não é possível cancelar uma fatura com status '%s'.", getDescription()));
    }

    @Override
    protected void pendingInvoice() {
        throw new InvoiceStateException(
                String.format("Não é possível pendenciar uma fatura com status '%s'.", getDescription()));
    }

    @Override
    protected void expiredInvoice() {
        throw new InvoiceStateException(
                String.format("A fatura não pode estar expirada com status '%s'.", getDescription()));
    }

    @Override
    public InvoiceStatus getDescription() {
        return InvoiceStatus.CANCELLED;
    }
}

package br.com.fiap.fastfood.api.core.domain.model.invoice.state;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice.InvoiceStatus;
import br.com.fiap.fastfood.api.core.domain.exception.InvoiceStateException;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.invoice.InvoiceState;

public class InvoiceExpiredState extends InvoiceState {

    public InvoiceExpiredState(Invoice invoice) {
        super(invoice);
    }

    @Override
    public InvoiceStatus getDescription() {
        return InvoiceStatus.CANCELLED;
    }

    @Override
    public void payInvoice() {
        throw new InvoiceStateException(
                String.format("Não é possível pagar uma fatura com status '%s'.", getDescription()));
    }

    @Override
    public void paidInvoice() {
        throw new InvoiceStateException(
                String.format("Não é possível pagar uma fatura com status '%s'.", getDescription()));
    }

    @Override
    public void cancelInvoice() {
        throw new InvoiceStateException(
                String.format("Não é possível cancelar uma fatura com status '%s'.", getDescription()));
    }

    @Override
    public void pendingInvoice() {
        throw new InvoiceStateException(
                String.format("Não é possível pendenciar uma fatura com status '%s'.", getDescription()));
    }

    @Override
    public void expiredInvoice() {
        this.invoice.changeState(new InvoiceExpiredState(this.invoice));
    }
}

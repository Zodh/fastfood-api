package br.com.fiap.fastfood.api.core.domain.model.invoice.state;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice.InvoiceStatus;
import br.com.fiap.fastfood.api.core.domain.exception.InvoiceStateException;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.invoice.InvoiceState;

public class InvoicePendingState extends InvoiceState {

    public InvoicePendingState(Invoice invoice) {
        super(invoice);
    }

    @Override
    public InvoiceStatus getDescription() {
        return InvoiceStatus.PENDING;
    }

    @Override
    public void payInvoice() {
        this.invoice.changeState(new InvoicePaidState(this.invoice));
    }

    @Override
    public void paidInvoice() {
        throw new InvoiceStateException(
                String.format("Não é possível a fatura estar paga com status '%s'.", getDescription()));
    }

    @Override
    public void cancelInvoice() {
        this.invoice.changeState(new InvoiceCancelledState(this.invoice));
    }

    @Override
    public void pendingInvoice() {
        this.invoice.changeState(new InvoicePendingState(this.invoice));
    }

    @Override
    public void expiredInvoice() {
        this.invoice.changeState(new InvoiceExpiredState(this.invoice));
    }
}
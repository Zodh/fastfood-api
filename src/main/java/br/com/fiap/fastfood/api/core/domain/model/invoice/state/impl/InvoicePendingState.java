package br.com.fiap.fastfood.api.core.domain.model.invoice.state.impl;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice.InvoiceStatus;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.InvoiceState;

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
    public void cancelInvoice() {
        this.invoice.changeState(new InvoiceCancelledState(this.invoice));
    }

    @Override
    public void expireInvoice() {
        this.invoice.changeState(new InvoiceExpiredState(this.invoice));
    }
}
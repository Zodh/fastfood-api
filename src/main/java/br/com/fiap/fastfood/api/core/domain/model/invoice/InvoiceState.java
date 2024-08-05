package br.com.fiap.fastfood.api.core.domain.model.invoice;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice.InvoiceStatus;

public abstract class InvoiceState {

    protected Invoice invoice;

    public InvoiceState(Invoice invoice) {
        this.invoice = invoice;
    }

    protected abstract void payInvoice();
    protected abstract void paidInvoice();
    protected abstract void cancelInvoice();
    protected abstract void pendingInvoice();
    protected abstract void expiredInvoice();

    public abstract InvoiceStatus getDescription();
}

package br.com.fiap.fastfood.api.entities.invoice.state;

import br.com.fiap.fastfood.api.entities.invoice.Invoice;

import java.math.BigDecimal;

public abstract class InvoiceState {

    protected Invoice invoice;

    public InvoiceState(Invoice invoice) {
        this.invoice = invoice;
    }

    public abstract void payInvoice();
    public abstract void cancelInvoice();
    public abstract void expireInvoice();
    public abstract void setPrice(BigDecimal price);

    public abstract InvoiceStateEnum getDescription();
}

package br.com.fiap.fastfood.api.core.domain.model.invoice.state.impl;

import br.com.fiap.fastfood.api.core.domain.model.invoice.state.InvoiceStateEnum;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.InvoiceState;
import java.math.BigDecimal;
import java.util.Objects;

public class InvoicePendingState extends InvoiceState {

    public InvoicePendingState(Invoice invoice) {
        super(invoice);
    }

    @Override
    public InvoiceStateEnum getDescription() {
        return InvoiceStateEnum.PENDING;
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

    @Override
    public void setPrice(BigDecimal price) {
        if (Objects.isNull(price)) {
            this.invoice.setPrice(BigDecimal.ZERO);
        }
        this.invoice.setPrice(price);
    }
}
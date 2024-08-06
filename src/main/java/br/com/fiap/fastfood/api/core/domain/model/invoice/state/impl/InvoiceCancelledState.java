package br.com.fiap.fastfood.api.core.domain.model.invoice.state.impl;

import br.com.fiap.fastfood.api.core.domain.model.invoice.state.InvoiceStateEnum;
import br.com.fiap.fastfood.api.core.domain.exception.InvoiceStateException;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.InvoiceState;
import java.math.BigDecimal;

public class InvoiceCancelledState extends InvoiceState {
    public InvoiceCancelledState(Invoice invoice) {
        super(invoice);
    }

    @Override
    public void payInvoice() {
        throw new InvoiceStateException(
                String.format("Não é possível pagar uma fatura com status '%s'.", getDescription()));
    }

    @Override
    public void cancelInvoice() {
        throw new InvoiceStateException(
                String.format("Não é possível cancelar uma fatura com status '%s'.", getDescription()));
    }

    @Override
    public void expireInvoice() {
        throw new InvoiceStateException(
                String.format("A fatura não pode estar expirada com status '%s'.", getDescription()));
    }

    @Override
    public void setPrice(BigDecimal price) {
        throw new InvoiceStateException(String.format("Não é possível alterar o valor da fatura quando ela já está %s!", getDescription()));
    }

    @Override
    public InvoiceStateEnum getDescription() {
        return InvoiceStateEnum.CANCELLED;
    }
}

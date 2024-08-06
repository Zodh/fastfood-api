package br.com.fiap.fastfood.api.core.domain.model.invoice.state.impl;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.invoice.InvoiceStatus;
import br.com.fiap.fastfood.api.core.domain.exception.InvoiceStateException;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.InvoiceState;

public class InvoicePaidState extends InvoiceState {

    public InvoicePaidState(Invoice invoice) {
        super(invoice);
    }

    @Override
    public InvoiceStatus getDescription() {
        return InvoiceStatus.PAID;
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
                String.format("Não é possível expirar uma fatura com status '%s'.", getDescription()));
    }
}

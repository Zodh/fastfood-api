package br.com.fiap.fastfood.api.core.application.ports.repository;

import br.com.fiap.fastfood.api.core.application.ports.BaseRepository;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;

public interface InvoiceRepositoryPort extends BaseRepository<Invoice, Long> {

    void expireOldInvoices();
}

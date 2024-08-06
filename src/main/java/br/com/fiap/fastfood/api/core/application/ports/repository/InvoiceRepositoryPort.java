package br.com.fiap.fastfood.api.core.application.ports.repository;

import br.com.fiap.fastfood.api.core.application.ports.BaseRepository;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import java.util.List;

public interface InvoiceRepositoryPort extends BaseRepository<Invoice, Long> {

    void expireOldInvoices(int timeInMinutes);
    void cancelPendingInvoicesByOrder(Long orderId);
    List<Invoice> findByOrderId(Long orderId);

}

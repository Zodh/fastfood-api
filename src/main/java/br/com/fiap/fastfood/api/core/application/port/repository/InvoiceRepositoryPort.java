package br.com.fiap.fastfood.api.core.application.port.repository;

import br.com.fiap.fastfood.api.core.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.core.application.port.BaseRepository;
import java.util.List;

public interface InvoiceRepositoryPort extends BaseRepository<InvoiceDTO, Long> {

    void expireOldInvoices(int timeInMinutes);
    void cancelPendingInvoicesByOrder(Long orderId);
    List<InvoiceDTO> findByOrderId(Long orderId);

}

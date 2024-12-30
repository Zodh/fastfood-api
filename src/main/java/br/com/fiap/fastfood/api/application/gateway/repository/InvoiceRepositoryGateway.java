package br.com.fiap.fastfood.api.application.gateway.repository;

import br.com.fiap.fastfood.api.application.dto.invoice.InvoiceDTO;
import java.util.List;

public interface InvoiceRepositoryGateway extends BaseRepositoryGateway<InvoiceDTO, Long> {

    void expireOldInvoices(int timeInMinutes);
    void cancelPendingInvoicesByOrder(Long orderId);
    List<InvoiceDTO> findByOrderId(Long orderId);

}

package br.com.fiap.fastfood.api.core.application.port.inbound.service;

import br.com.fiap.fastfood.api.core.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import java.util.List;

public interface InvoiceServicePort {

    InvoiceDTO executeFakeCheckout(OrderDTO order);
    List<InvoiceDTO> getInvoicesByOrder(Long orderId);

}

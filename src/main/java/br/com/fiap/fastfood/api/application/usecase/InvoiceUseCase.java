package br.com.fiap.fastfood.api.application.usecase;

import br.com.fiap.fastfood.api.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.application.dto.order.OrderDTO;
import java.util.List;

public interface InvoiceUseCase {

    InvoiceDTO executeFakeCheckout(OrderDTO order);
    List<InvoiceDTO> getInvoicesByOrder(Long orderId);

}

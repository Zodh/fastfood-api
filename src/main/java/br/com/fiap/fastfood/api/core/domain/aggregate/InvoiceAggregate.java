package br.com.fiap.fastfood.api.core.domain.aggregate;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.impl.InvoicePaidState;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.impl.InvoicePendingState;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceAggregate {

    private Invoice invoice;

    public Invoice createInvoice(Order order) {
        if (Objects.isNull(order) || Objects.isNull(order.getPrice())) {
            throw new DomainException(new ErrorDetail("order", "Não é possível criar um pagamento para um pedido nulo ou sem um preço definido!"));
        }
        BigDecimal paidAmount = BigDecimal.ZERO;
        if (Objects.nonNull(order.getInvoices()) && !order.getInvoices().isEmpty() && order.getInvoices().stream().anyMatch(i -> i.getState() instanceof InvoicePaidState)) {
            paidAmount = order.getInvoices().stream()
                .filter(i -> Objects.nonNull(i) && Objects.nonNull(i.getState())
                    && i.getState() instanceof InvoicePaidState && Objects.nonNull(i.getPrice()))
                .map(Invoice::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (paidAmount.compareTo(order.getPrice()) > 0) {
                throw new DomainException(new ErrorDetail("order.price", "O valor pago é maior do que o valor do pedido! Contate um administrador!"));
            }
        }
        this.invoice = new Invoice();
        invoice.changeState(new InvoicePendingState(invoice));
        invoice.getState().setPrice(order.getPrice().subtract(paidAmount));
        invoice.setOrder(order);
        invoice.setCreatedAt(LocalDateTime.now());
        return invoice;
    }

    public void pay() {
        invoice.getState().payInvoice();
    }
}

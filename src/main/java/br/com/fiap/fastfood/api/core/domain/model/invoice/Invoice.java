package br.com.fiap.fastfood.api.core.domain.model.invoice;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.InvoiceState;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Invoice {

    private Long id;

    private InvoiceState state;

    private BigDecimal price;

    private Long externalInvoiceId;

    private LocalDateTime created;

    private LocalDateTime updated;

    private InvoiceVendor vendor;

    private Order order;

    public void changeState(InvoiceState state) {
        if (Objects.isNull(state)) {
            throw new DomainException(new ErrorDetail("invoice.state", "O estado do pagamento não pode ser nulo!"));
        }
        this.state = state;
    }

    public void cancel() {
        this.getState().cancelInvoice();
    }

    public void expire() {
        this.getState().expireInvoice();
    }
}

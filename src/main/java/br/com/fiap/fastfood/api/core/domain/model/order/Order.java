package br.com.fiap.fastfood.api.core.domain.model.order;

import br.com.fiap.fastfood.api.core.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.invoice.state.impl.InvoicePendingState;
import br.com.fiap.fastfood.api.core.domain.model.order.state.OrderState;
import br.com.fiap.fastfood.api.core.domain.model.person.Collaborator;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

@Setter
@Getter
public class Order {

  private Long id;
  private OrderState state;
  private List<OrderProduct> products;
  private Collaborator collaborator;
  private Customer customer;
  private BigDecimal price;
  private List<Invoice> invoices;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public void changeState(OrderState state) {
    if (Objects.isNull(state)) {
      throw new DomainException(new ErrorDetail("order.state", "O estado do pedido não pode ser nulo!"));
    }
    this.state = state;
  }

  public void calculatePrice() {
    this.price = Optional.ofNullable(products)
        .orElse(new ArrayList<>())
        .stream()
        .filter(op -> Objects.nonNull(op) && Objects.nonNull(op.getPrice()))
        .map(OrderProduct::getPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public Invoice getInvoice() {
    return Optional.ofNullable(getActiveInvoiceOrNull()).orElseThrow(() -> new NotFoundException("Não foi encontrada nenhuma cobrança ativa para esse pedido!"));
  }

  public Invoice getActiveInvoiceOrNull() {
    return Optional.ofNullable(invoices).orElse(new ArrayList<>()).stream()
        .filter(current -> current.getState() instanceof InvoicePendingState)
        .findFirst()
        .orElse(null);
  }

  public OrderProduct findProductById(Long productId) {
    return Optional.ofNullable(this.getProducts()).orElse(new ArrayList<>()).stream().filter(op -> Objects.nonNull(op) && Objects.equals(
        op.getId(), productId)).findFirst().orElseThrow(() -> new DomainException(new ErrorDetail("order.products", String.format("Não foi encontrado um produto com o identificador %d", productId))));
  }

  public boolean hasInvoice() {
    return !CollectionUtils.isEmpty(this.invoices) && this.invoices.stream().anyMatch(i -> i.getState() instanceof InvoicePendingState);
  }

}

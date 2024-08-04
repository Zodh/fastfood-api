package br.com.fiap.fastfood.api.core.domain.model.order;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.order.state.OrderState;
import br.com.fiap.fastfood.api.core.domain.model.person.Collaborator;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Order {

  private Long id;
  private OrderState state;
  private List<OrderProduct> products;
  private Collaborator collaborator;
  private Customer customer;
  private BigDecimal price;
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
        .orElse(Collections.emptyList())
        .stream()
        .filter(op -> Objects.nonNull(op) && Objects.nonNull(op.getPrice()))
        .map(OrderProduct::getPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

}

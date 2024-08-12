package br.com.fiap.fastfood.api.core.domain.aggregate;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;
import java.util.Objects;

public class OrderAggregate {

  private final Order root;

  public OrderAggregate(Order root) {
    if (Objects.isNull(root)) {
      throw new DomainException(new ErrorDetail("order", "O pedido não pode ser nulo!"));
    }
    this.root = root;
  }

  public void includeOrderProduct(OrderProduct orderProduct) {
    root.getState().includeOrderProduct(orderProduct);
    root.calculatePrice();
  }

  public void removeOrderProduct(Long orderProductId) {
    root.getState().removeOrderProduct(orderProductId);
    root.calculatePrice();
  }

  public void confirmOrder() {
    root.getState().confirmOrder();
  }

  public void cancelOrder() {
    root.getState().cancelOrder();
  }

  public void includeOptionalInProduct() {
    root.getState().includeOptionalInProduct();
  }

  public void removeOptionalFromProduct() {
    root.getState().removeOptionalFromProduct();
  }

  public void updateShouldRemoveIngredient(Long productId, Long ingredientId, boolean shouldRemove) {
    root.getState().updateIngredientRemoval(productId, ingredientId, shouldRemove);
  }

}

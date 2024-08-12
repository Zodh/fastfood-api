package br.com.fiap.fastfood.api.core.domain.model.order.state.impl;

import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.model.order.OrderStateEnum;
import br.com.fiap.fastfood.api.core.domain.model.order.state.OrderState;
import br.com.fiap.fastfood.api.core.domain.model.person.Collaborator;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;

public class OrderInPreparationState extends OrderState {

  public OrderInPreparationState(Order order) {
    super(order);
  }

  @Override
  public void includeOrderProduct(OrderProduct orderProduct) {
    throw new OrderOperationNotAllowedException();
  }

  @Override
  public void removeOrderProduct(Long orderProductId) {
    throw new OrderOperationNotAllowedException();
  }

  @Override
  public void confirmOrder() {
    throw new OrderOperationNotAllowedException();
  }

  @Override
  public void cancelOrder() {
    this.order.changeState(new OrderCancelledState(this.order));
  }

  @Override
  public void setAwaitingPreparation() {
    throw new OrderOperationNotAllowedException();
  }

  @Override
  public void initializePreparation() {
    throw new OrderOperationNotAllowedException();
  }

  @Override
  public void setReadyToCollection() {
    this.order.changeState(new OrderReadyState(this.order));
  }

  @Override
  public void finish() {
    throw new OrderOperationNotAllowedException();
  }

  @Override
  public void setCustomer(Customer customer) {
    throw new OrderOperationNotAllowedException();
  }

  @Override
  public void setCollaborator(Collaborator collaborator) {
    throw new OrderOperationNotAllowedException();
  }

  @Override
  public void includeOptionalInProduct() {
    throw new OrderOperationNotAllowedException();
  }

  @Override
  public void removeOptionalFromProduct() {
    new OrderOperationNotAllowedException();
  }

  @Override
  public void updateIngredientRemoval(Long productId, Long ingredientId, boolean shouldRemove) {
    new OrderOperationNotAllowedException();
  }

  @Override
  public OrderStateEnum getDescription() {
    return OrderStateEnum.IN_PREPARATION;
  }
}

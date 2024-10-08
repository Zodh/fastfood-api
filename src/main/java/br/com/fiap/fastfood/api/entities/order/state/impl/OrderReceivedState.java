package br.com.fiap.fastfood.api.entities.order.state.impl;

import br.com.fiap.fastfood.api.entities.order.Order;
import br.com.fiap.fastfood.api.entities.order.OrderStateEnum;
import br.com.fiap.fastfood.api.entities.order.state.OrderState;
import br.com.fiap.fastfood.api.entities.person.Collaborator;
import br.com.fiap.fastfood.api.entities.person.Customer;
import br.com.fiap.fastfood.api.entities.product.OrderProduct;

// TODO: Esse estado será utilizado pelo checkout fake. Criado em: 03/08/2024 ás 04:48:49.
public class OrderReceivedState extends OrderState {

  public OrderReceivedState(Order order) {
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
    this.order.changeState(new OrderInPreparationState(this.order));
  }

  @Override
  public void setReadyToCollection() {
    throw new OrderOperationNotAllowedException();
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
    return OrderStateEnum.RECEIVED;
  }
}

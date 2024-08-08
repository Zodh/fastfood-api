package br.com.fiap.fastfood.api.core.domain.model.order.state.impl;

import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.model.order.OrderStateEnum;
import br.com.fiap.fastfood.api.core.domain.model.order.state.OrderState;
import br.com.fiap.fastfood.api.core.domain.model.person.Collaborator;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;

// TODO: Esse estado será utilizado pelo checkout fake. Criado em: 03/08/2024 ás 04:48:49.
public class OrderAwaitingPreparationState extends OrderState {

  public OrderAwaitingPreparationState(Order order) {
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
  public OrderStateEnum getDescription() {
    return OrderStateEnum.AWAITING_PREPARATION;
  }
}

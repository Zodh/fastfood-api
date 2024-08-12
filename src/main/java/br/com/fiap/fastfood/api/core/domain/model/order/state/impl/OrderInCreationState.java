package br.com.fiap.fastfood.api.core.domain.model.order.state.impl;

import br.com.fiap.fastfood.api.core.domain.exception.DomainException;
import br.com.fiap.fastfood.api.core.domain.exception.ErrorDetail;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.model.order.OrderStateEnum;
import br.com.fiap.fastfood.api.core.domain.model.order.state.OrderState;
import br.com.fiap.fastfood.api.core.domain.model.person.Collaborator;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class OrderInCreationState extends OrderState {

  public OrderInCreationState(Order order) {
    super(order);
  }

  @Override
  public void includeOrderProduct(OrderProduct orderProduct) {
    if (Objects.isNull(this.order.getProducts())) {
      this.order.setProducts(new ArrayList<>());
    }
    this.order.getProducts().add(orderProduct);
  }

  @Override
  public void removeOrderProduct(Long orderProductId) {
    List<OrderProduct> products = this.order.getProducts();
    if (CollectionUtils.isEmpty(products) || this.order.getProducts().stream().noneMatch(op -> op.getId().equals(orderProductId))) {
      throw new DomainException(new ErrorDetail("order.products", "Não foi encontrado nenhum produto com o identificador informado no pedido!"));
    }
    List<OrderProduct> filtered = products.stream().filter(op -> Objects.nonNull(op) && !op.getId().equals(orderProductId)).collect(
        Collectors.toList());
    this.order.setProducts(filtered);
  }

  @Override
  public void confirmOrder() {
    if (CollectionUtils.isEmpty(this.order.getProducts())) {
      throw new DomainException(new ErrorDetail("order.products", "Os produtos do pedido não podem ser vazios!"));
    }
    this.order.changeState(new OrderAwaitingPaymentState(this.order));
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
    throw new OrderOperationNotAllowedException();
  }

  @Override
  public void finish() {
    throw new OrderOperationNotAllowedException();
  }

  @Override
  public void setCustomer(Customer customer) {
    if (Objects.isNull(customer)) {
      return;
    }
    this.order.setCustomer(customer);
  }

  @Override
  public void setCollaborator(Collaborator collaborator) {
    if (Objects.isNull(collaborator)) {
      return;
    }
    this.order.setCollaborator(collaborator);
  }

  @Override
  public void includeOptionalInProduct() {
    // Do nothing.
  }

  @Override
  public void removeOptionalFromProduct() {
    // Do nothing.
  }

  @Override
  public void updateIngredientRemoval(Long productId, Long ingredientId, boolean shouldRemove) {
    OrderProduct product = this.order.findProductById(productId);
    OrderProduct ingredient = product.findIngredientById(ingredientId);
    if (product.getIngredients().stream().filter(i -> Objects.nonNull(i) && !i.isShouldRemove()).count() == 1 && shouldRemove) {
      throw new DomainException(new ErrorDetail("order.products.ingredients", "O produto deve conter ao menos um ingrediente!"));
    }
    ingredient.setShouldRemove(shouldRemove);

    ingredient.calculateCost();
    ingredient.calculatePrice();

    product.calculateCost();
    product.calculatePrice();

    this.order.calculatePrice();
  }

  @Override
  public OrderStateEnum getDescription() {
    return OrderStateEnum.IN_CREATION;
  }
}

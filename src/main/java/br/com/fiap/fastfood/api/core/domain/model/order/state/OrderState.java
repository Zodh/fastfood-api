package br.com.fiap.fastfood.api.core.domain.model.order.state;

import br.com.fiap.fastfood.api.core.domain.model.order.OrderStateEnum;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.model.person.Collaborator;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;

public abstract class OrderState {

  protected Order order;

  public OrderState(Order order) {
    this.order = order;
  }

  public abstract void includeOrderProduct(OrderProduct orderProduct);
  public abstract void removeOrderProduct(Long orderProductId);
  public abstract void confirmOrder(); // Neste momento, o pedido será cobrado.
  public abstract void cancelOrder();
  public abstract void setAwaitingPreparation();
  public abstract void initializePreparation();
  public abstract void setReadyToCollection();
  public abstract void finish();
  public abstract void setCustomer(Customer customer);
  public abstract void setCollaborator(Collaborator collaborator);
  public abstract void includeOptionalInProduct();
  public abstract void removeOptionalFromProduct();
  public abstract void updateIngredientRemoval(Long productId, Long ingredientId, boolean shouldRemove);

  public abstract OrderStateEnum getDescription();

}

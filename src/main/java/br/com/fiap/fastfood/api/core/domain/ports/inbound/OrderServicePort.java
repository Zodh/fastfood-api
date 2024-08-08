package br.com.fiap.fastfood.api.core.domain.ports.inbound;

import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.model.person.Collaborator;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;

public interface OrderServicePort {

  Order create(Customer customer, Collaborator collaborator);
  Order includeOrderProduct(Long orderId, OrderProduct orderProduct);
  Order getById(Long orderId);
  Order removeOrderProduct(Long orderId, Long orderProductId);
  Order cancel(Long orderId);
  Order confirm(Long orderId);

  // TODO: When security layer comes, this method should receive also customer that started preparation. Criado em: 07/08/2024 ás 18:39:08.
  void turnReadyToPrepare(Long orderId);
  void prepare(Long orderId);
  void turnReadyToPick(Long orderId);
  void finish(Long orderId);

}

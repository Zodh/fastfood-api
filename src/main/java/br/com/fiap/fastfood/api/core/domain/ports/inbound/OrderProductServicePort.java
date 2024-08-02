package br.com.fiap.fastfood.api.core.domain.ports.inbound;

import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;

public interface OrderProductServicePort {


  void create(OrderProduct orderProduct);
  void includeOptional(Long orderProductId, OrderProduct optional);
  void removeOptional(Long orderProductId, Long optionalId);
  void updateShouldRemove(Long orderProductId, Long ingredientId, boolean shouldRemove);
  void delete(Long id);
  OrderProduct getById(Long id);

}

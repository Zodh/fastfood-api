package br.com.fiap.fastfood.api.core.domain.ports.inbound;

import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;

public interface OrderProductServicePort {


  OrderProduct create(OrderProduct orderProduct);
  OrderProduct includeOptional(Long orderProductId, OrderProduct optional);
  OrderProduct removeOptional(Long orderProductId, Long optionalId);
  OrderProduct updateShouldRemove(Long orderProductId, Long ingredientId, boolean shouldRemove);
  void delete(Long id);
  OrderProduct getById(Long id);

}

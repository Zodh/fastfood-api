package br.com.fiap.fastfood.api.core.application.port.inbound.service;

import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.application.dto.product.OrderProductDTO;

public interface OrderProductServicePort {


  OrderProductDTO create(OrderDTO order, OrderProductDTO orderProduct);
  OrderProductDTO includeOptional(Long orderProductId, OrderProductDTO optional);
  OrderProductDTO removeOptional(Long orderProductId, Long optionalId);
  OrderProductDTO updateShouldRemove(Long orderProductId, Long ingredientId, boolean shouldRemove);
  void delete(Long id);
  OrderProductDTO getById(Long id);
  OrderProductDTO validateAndDetail(OrderProductDTO orderProduct);

}

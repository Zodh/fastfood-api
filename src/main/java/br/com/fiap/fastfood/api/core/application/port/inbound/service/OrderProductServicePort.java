package br.com.fiap.fastfood.api.core.application.port.inbound.service;

import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.application.dto.product.OrderProductDTO;

public interface OrderProductServicePort {


  OrderProductDTO create(OrderDTO order, OrderProductDTO orderProduct);
  OrderProductDTO includeOptional(OrderDTO orderDTO, Long orderProductId, OrderProductDTO optional);
  OrderProductDTO removeOptional(OrderDTO orderDTO, Long orderProductId, Long optionalId);
  OrderProductDTO updateShouldRemove(OrderDTO orderDTO, Long orderProductId, Long ingredientId, boolean shouldRemove);
  void delete(Long id);
  OrderProductDTO getById(Long id);
  OrderProductDTO validateAndDetail(OrderProductDTO orderProduct);
  OrderProductDTO save(OrderProductDTO orderProductDTO);
  OrderProductDTO save(OrderDTO orderDTO, OrderProductDTO orderProductDTO);
}

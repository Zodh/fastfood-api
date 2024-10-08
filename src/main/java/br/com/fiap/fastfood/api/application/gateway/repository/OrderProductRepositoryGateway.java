package br.com.fiap.fastfood.api.application.gateway.repository;

import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.application.dto.product.OrderProductDTO;
import java.util.List;

public interface OrderProductRepositoryGateway extends
    BaseRepositoryGateway<OrderProductDTO, Long> {

  OrderProductDTO save(OrderDTO order, OrderProductDTO orderProduct);
  void deleteOptionals(List<Long> ids);
  void deleteIngredients(List<Long> ids);
  void deleteAllById(List<Long> ids);

}

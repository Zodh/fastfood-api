package br.com.fiap.fastfood.api.core.application.port.repository;

import br.com.fiap.fastfood.api.core.application.port.BaseRepository;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;
import java.util.List;

public interface OrderProductRepositoryPort extends BaseRepository<OrderProduct, Long> {

  OrderProduct save(Order order, OrderProduct orderProduct);
  void deleteOptionals(List<Long> ids);
  void deleteIngredients(List<Long> ids);
  void deleteAllById(List<Long> ids);

}

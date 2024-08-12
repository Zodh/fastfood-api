package br.com.fiap.fastfood.api.core.application.port.repository;

import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.application.dto.product.OrderProductDTO;
import br.com.fiap.fastfood.api.core.application.port.BaseRepository;
import java.util.List;

public interface OrderProductRepositoryPort extends BaseRepository<OrderProductDTO, Long> {

  OrderProductDTO save(OrderDTO order, OrderProductDTO orderProduct);
  void deleteOptionals(List<Long> ids);
  void deleteIngredients(List<Long> ids);
  void deleteAllById(List<Long> ids);

}

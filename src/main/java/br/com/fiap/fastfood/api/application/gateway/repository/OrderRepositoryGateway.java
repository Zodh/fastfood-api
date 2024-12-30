package br.com.fiap.fastfood.api.application.gateway.repository;

import br.com.fiap.fastfood.api.application.dto.order.OrderDTO;
import java.util.List;

public interface OrderRepositoryGateway extends BaseRepositoryGateway<OrderDTO, Long> {

  List<OrderDTO> findAll();

}

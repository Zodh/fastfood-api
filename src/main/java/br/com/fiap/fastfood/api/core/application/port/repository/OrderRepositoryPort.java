package br.com.fiap.fastfood.api.core.application.port.repository;

import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.application.port.BaseRepository;
import java.util.List;

public interface OrderRepositoryPort extends BaseRepository<OrderDTO, Long> {

  List<OrderDTO> findAll();

}

package br.com.fiap.fastfood.api.core.application.port.repository;

import br.com.fiap.fastfood.api.core.application.port.BaseRepository;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;

public interface OrderRepositoryPort extends BaseRepository<Order, Long> {

}

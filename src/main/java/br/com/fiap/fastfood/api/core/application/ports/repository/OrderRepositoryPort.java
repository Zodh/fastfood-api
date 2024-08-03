package br.com.fiap.fastfood.api.core.application.ports.repository;

import br.com.fiap.fastfood.api.core.application.ports.BaseRepository;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;

public interface OrderRepositoryPort extends BaseRepository<Order, Long> {

}

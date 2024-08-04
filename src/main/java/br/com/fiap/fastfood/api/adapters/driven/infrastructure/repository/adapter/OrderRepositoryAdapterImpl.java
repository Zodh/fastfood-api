package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.order.OrderEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.order.OrderRepository;
import br.com.fiap.fastfood.api.core.application.ports.repository.OrderRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderRepositoryAdapterImpl implements OrderRepositoryPort {

  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;

  @Autowired
  public OrderRepositoryAdapterImpl(OrderRepository orderRepository, OrderMapper orderMapper) {
    this.orderRepository = orderRepository;
    this.orderMapper = orderMapper;
  }

  @Override
  public Optional<Order> findById(Long identifier) {
    Optional<OrderEntity> orderEntityOptional = orderRepository.findById(identifier);
    Optional<Order> order = orderEntityOptional.map(orderMapper::toDomain);
    order.ifPresent(value -> value.setState(orderMapper.mapStateImpl(orderEntityOptional.get()
        .getState(), order.get())));
    return order;
  }

  @Override
  public Order save(Order data) {
    OrderEntity entity = orderMapper.toEntity(data);
    OrderEntity persistedEntity = orderRepository.save(entity);
    Order order = orderMapper.toDomain(persistedEntity);
    order.setState(orderMapper.mapStateImpl(persistedEntity.getState(), order));
    return order;
  }

  @Override
  public boolean delete(Long identifier) {
    orderRepository.deleteById(identifier);
    return true;
  }
}

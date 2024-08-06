package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.order.OrderEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.OrderProductEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderMapperImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderProductMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderProductMapperImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.product.OrderProductRepository;
import br.com.fiap.fastfood.api.core.application.ports.repository.OrderProductRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderProductRepositoryAdapterImpl implements OrderProductRepositoryPort {

  private final OrderProductRepository repository;
  private final OrderProductMapper orderProductMapper;
  private final OrderMapper orderMapper;

  @Autowired
  public OrderProductRepositoryAdapterImpl(OrderProductRepository repository) {
    this.repository = repository;
    this.orderProductMapper = new OrderProductMapperImpl();
    this.orderMapper = new OrderMapperImpl();
  }

  @Override
  public Optional<OrderProduct> findById(Long identifier) {
    Optional<OrderProductEntity> entity = repository.findById(identifier);
    return entity.map(orderProductMapper::toDomain);
  }

  @Override
  public OrderProduct save(OrderProduct data) {
    OrderProductEntity entity = orderProductMapper.toEntity(data);
    OrderProductEntity persistedEntity = repository.save(entity);
    return orderProductMapper.toDomain(persistedEntity);
  }

  @Override
  public boolean delete(Long identifier) {
    repository.deleteById(identifier);
    return true;
  }

  @Override
  public OrderProduct save(Order order, OrderProduct orderProduct) {
    OrderEntity orderEntity = orderMapper.toEntity(order);
    OrderProductEntity orderProductEntity = orderProductMapper.toEntity(orderProduct);
    orderProductEntity.setOrder(orderEntity);
    OrderProductEntity persistedOrderProductEntity = repository.save(orderProductEntity);
    return orderProductMapper.toDomain(persistedOrderProductEntity);
  }

  @Override
  public void deleteOptionals(List<Long> ids) {
    repository.deleteOptionalByIds(ids);
  }

  @Override
  public void deleteIngredients(List<Long> ids) {
    repository.deleteIngredientByIds(ids);
  }

  @Override
  public void deleteAllById(List<Long> ids) {
    repository.deleteAllById(ids);
  }
}

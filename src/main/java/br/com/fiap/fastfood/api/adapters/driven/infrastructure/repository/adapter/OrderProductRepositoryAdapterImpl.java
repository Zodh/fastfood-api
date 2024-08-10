package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.order.OrderEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.OrderProductEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderMapperImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderProductMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderProductMapperImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.product.OrderProductRepository;
import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.application.dto.product.OrderProductDTO;
import br.com.fiap.fastfood.api.core.application.port.repository.OrderProductRepositoryPort;
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
  public Optional<OrderProductDTO> findById(Long identifier) {
    Optional<OrderProductEntity> entity = repository.findById(identifier);
    return entity.map(orderProductMapper::toDTO);
  }

  @Override
  public OrderProductDTO save(OrderProductDTO data) {
    OrderProductEntity entity = orderProductMapper.toEntity(data);
    OrderProductEntity persistedEntity = repository.save(entity);
    return orderProductMapper.toDTO(persistedEntity);
  }

  @Override
  public boolean delete(Long identifier) {
    repository.deleteById(identifier);
    return true;
  }

  @Override
  public OrderProductDTO save(OrderDTO order, OrderProductDTO orderProduct) {
    OrderEntity orderEntity = orderMapper.toEntity(order);
    OrderProductEntity orderProductEntity = orderProductMapper.toEntity(orderProduct);
    orderProductEntity.setOrder(orderEntity);
    OrderProductEntity persistedOrderProductEntity = repository.save(orderProductEntity);
    return orderProductMapper.toDTO(persistedOrderProductEntity);
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

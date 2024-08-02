package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.product.OrderProductEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderProductMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.product.OrderProductRepository;
import br.com.fiap.fastfood.api.core.application.ports.repository.OrderProductRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.model.product.OrderProduct;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderProductRepositoryAdapterImpl implements OrderProductRepositoryPort {

  private final OrderProductRepository repository;
  private final OrderProductMapper orderProductMapper;

  @Autowired
  public OrderProductRepositoryAdapterImpl(OrderProductRepository repository, OrderProductMapper orderProductMapper) {
    this.repository = repository;
    this.orderProductMapper = orderProductMapper;
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

}

package br.com.fiap.fastfood.api.adapters.gateway;

import br.com.fiap.fastfood.api.adapters.mapper.OrderMapperInfraImpl;
import br.com.fiap.fastfood.api.adapters.mapper.OrderProductMapperInfraImpl;
import br.com.fiap.fastfood.api.infrastructure.dao.entity.order.OrderEntity;
import br.com.fiap.fastfood.api.infrastructure.dao.entity.product.OrderProductEntity;
import br.com.fiap.fastfood.api.adapters.mapper.OrderMapperInfra;
import br.com.fiap.fastfood.api.adapters.mapper.OrderProductMapperInfra;
import br.com.fiap.fastfood.api.infrastructure.dao.repository.product.OrderProductRepository;
import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.application.dto.product.OrderProductDTO;
import br.com.fiap.fastfood.api.application.gateway.repository.OrderProductRepositoryGateway;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderProductRepositoryAdapterImpl implements OrderProductRepositoryGateway {

  private final OrderProductRepository repository;
  private final OrderProductMapperInfra orderProductMapperInfra;
  private final OrderMapperInfra orderMapperInfra;

  @Autowired
  public OrderProductRepositoryAdapterImpl(OrderProductRepository repository) {
    this.repository = repository;
    this.orderProductMapperInfra = new OrderProductMapperInfraImpl();
    this.orderMapperInfra = new OrderMapperInfraImpl();
  }

  @Override
  public Optional<OrderProductDTO> findById(Long identifier) {
    Optional<OrderProductEntity> entity = repository.findById(identifier);
    return entity.map(orderProductMapperInfra::toDTO);
  }

  @Override
  public OrderProductDTO save(OrderProductDTO data) {
    OrderProductEntity entity = orderProductMapperInfra.toEntity(data);
    OrderProductEntity persistedEntity = repository.save(entity);
    return orderProductMapperInfra.toDTO(persistedEntity);
  }

  @Override
  public boolean delete(Long identifier) {
    repository.deleteById(identifier);
    return true;
  }

  @Override
  public OrderProductDTO save(OrderDTO order, OrderProductDTO orderProduct) {
    OrderEntity orderEntity = orderMapperInfra.toEntity(order);
    OrderProductEntity orderProductEntity = orderProductMapperInfra.toEntity(orderProduct);
    orderProductEntity.setOrder(orderEntity);
    OrderProductEntity persistedOrderProductEntity = repository.save(orderProductEntity);
    return orderProductMapperInfra.toDTO(persistedOrderProductEntity);
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
  @Transactional
  public void deleteAllById(List<Long> ids) {
    repository.deleteAllById(ids);
  }
}

package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.order.OrderEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.InvoiceMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.InvoiceMapperImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderMapperImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.order.OrderRepository;
import br.com.fiap.fastfood.api.core.application.port.repository.OrderRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderRepositoryAdapterImpl implements OrderRepositoryPort {

  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;
  private final InvoiceMapper invoiceMapper;

  @Autowired
  public OrderRepositoryAdapterImpl(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
    this.orderMapper = new OrderMapperImpl();
    this.invoiceMapper = new InvoiceMapperImpl();
  }

  @Override
  public Optional<Order> findById(Long identifier) {
    Optional<OrderEntity> orderEntityOptional = orderRepository.findById(identifier);
    Optional<Order> orderOpt = orderEntityOptional.map(orderMapper::toDomain);
    if (orderEntityOptional.isPresent() && orderOpt.isPresent()) {
      OrderEntity entity = orderEntityOptional.get();
      Order order = orderOpt.get();
      List<Invoice> invoices = Optional.ofNullable(entity.getOrderInvoices()).orElse(Collections.emptyList()).stream().map(i -> {
        Invoice invoice = invoiceMapper.toDomain(i);
        invoiceMapper.mapStateImpl(i.getState(), invoice);
        return invoice;
      }).collect(
          Collectors.toList());
      order.setInvoices(invoices);
      order.setState(orderMapper.mapStateImpl(entity
          .getState(), order));
    }
    return orderOpt;
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

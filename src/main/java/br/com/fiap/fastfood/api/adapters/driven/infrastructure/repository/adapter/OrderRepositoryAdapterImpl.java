package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.order.OrderEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.InvoiceMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.InvoiceMapperImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderMapperImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.order.OrderRepository;
import br.com.fiap.fastfood.api.core.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.application.port.repository.OrderRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.model.invoice.Invoice;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import java.util.ArrayList;
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
  public Optional<OrderDTO> findById(Long identifier) {
    Optional<OrderEntity> orderEntityOptional = orderRepository.findById(identifier);
    Optional<OrderDTO> orderOpt = orderEntityOptional.map(orderMapper::toDTO);
    if (orderEntityOptional.isPresent() && orderOpt.isPresent()) {
      OrderEntity entity = orderEntityOptional.get();
      OrderDTO orderDTO = orderOpt.get();
      List<InvoiceDTO> invoices = Optional.ofNullable(entity.getOrderInvoices()).orElse(new ArrayList<>()).stream().map(
          invoiceMapper::toDTO).collect(
          Collectors.toList());
      orderDTO.setInvoices(invoices);
    }
    return orderOpt;
  }

  @Override
  public OrderDTO save(OrderDTO data) {
    OrderEntity entity = orderMapper.toEntity(data);
    OrderEntity persistedEntity = orderRepository.save(entity);
    return orderMapper.toDTO(persistedEntity);
  }

  @Override
  public boolean delete(Long identifier) {
    Optional<OrderEntity> orderEntityOpt = orderRepository.findById(identifier);
    if (orderEntityOpt.isEmpty()) {
      return false;
    }
    orderRepository.deleteById(identifier);
    return true;
  }
}

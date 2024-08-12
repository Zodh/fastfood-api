package br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.adapter;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.entity.order.OrderEntity;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.InvoiceMapperInfra;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.InvoiceMapperInfraImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderMapperInfra;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderMapperInfraImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.order.OrderRepository;
import br.com.fiap.fastfood.api.core.application.dto.invoice.InvoiceDTO;
import br.com.fiap.fastfood.api.core.application.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.application.port.repository.OrderRepositoryPort;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderRepositoryAdapterImpl implements OrderRepositoryPort {

  private final OrderRepository orderRepository;
  private final OrderMapperInfra orderMapperInfra;
  private final InvoiceMapperInfra invoiceMapperInfra;

  @Autowired
  public OrderRepositoryAdapterImpl(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
    this.orderMapperInfra = new OrderMapperInfraImpl();
    this.invoiceMapperInfra = new InvoiceMapperInfraImpl();
  }

  @Override
  public Optional<OrderDTO> findById(Long identifier) {
    Optional<OrderEntity> orderEntityOptional = orderRepository.findById(identifier);
    Optional<OrderDTO> orderOpt = orderEntityOptional.map(orderMapperInfra::toDTO);
    if (orderEntityOptional.isPresent() && orderOpt.isPresent()) {
      OrderEntity entity = orderEntityOptional.get();
      OrderDTO orderDTO = orderOpt.get();
      List<InvoiceDTO> invoices = Optional.ofNullable(entity.getOrderInvoices())
          .orElse(new ArrayList<>()).stream().map(
              invoiceMapperInfra::toDTO).collect(
              Collectors.toList());
      orderDTO.setInvoices(invoices);
    }
    return orderOpt;
  }

  @Override
  public OrderDTO save(OrderDTO data) {
    OrderEntity entity = orderMapperInfra.toEntity(data);
    OrderEntity persistedEntity = orderRepository.save(entity);
    return orderMapperInfra.toDTO(persistedEntity);
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

  @Override
  public List<OrderDTO> findAll() {
    return orderRepository.findAll().stream().map(orderMapperInfra::toDTO).collect(Collectors.toList());
  }
}

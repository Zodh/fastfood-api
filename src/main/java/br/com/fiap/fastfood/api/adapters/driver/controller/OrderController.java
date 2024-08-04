package br.com.fiap.fastfood.api.adapters.driver.controller;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.CollaboratorMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.CollaboratorMapperImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.CustomerMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.CustomerMapperImpl;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderMapper;
import br.com.fiap.fastfood.api.adapters.driven.infrastructure.mapper.OrderMapperImpl;
import br.com.fiap.fastfood.api.adapters.driver.dto.order.CreateOrderRequestDTO;
import br.com.fiap.fastfood.api.adapters.driver.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.model.person.Collaborator;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.OrderServicePort;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

  private final OrderServicePort orderServicePort;
  private final CustomerMapper customerMapper;
  private final CollaboratorMapper collaboratorMapper;
  private final OrderMapper orderMapper;

  @Autowired
  public OrderController(OrderServicePort orderServicePort) {
    this.orderServicePort = orderServicePort;
    this.customerMapper = new CustomerMapperImpl();
    this.collaboratorMapper = new CollaboratorMapperImpl();
    this.orderMapper = new OrderMapperImpl();
  }

  @PostMapping
  public ResponseEntity<OrderDTO> create(@RequestBody(required = false) CreateOrderRequestDTO request) {
    Customer customer = null;
    Collaborator collaborator = null;
    if (Objects.nonNull(request)) {
      customer = customerMapper.toDomain(request.getCustomer());
      collaborator = collaboratorMapper.toDomain(request.getCollaborator());
    }
    Order order = orderServicePort.create(customer, collaborator);
    OrderDTO orderDTO = orderMapper.toDto(order);
    return ResponseEntity.status(HttpStatus.OK).body(orderDTO);
  }

}

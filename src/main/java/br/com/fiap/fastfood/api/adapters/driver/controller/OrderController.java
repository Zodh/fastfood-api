package br.com.fiap.fastfood.api.adapters.driver.controller;

import br.com.fiap.fastfood.api.adapters.driver.dto.order.CreateOrderRequestDTO;
import br.com.fiap.fastfood.api.adapters.driver.dto.order.OrderDTO;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.OrderServicePort;
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

  @Autowired
  public OrderController(OrderServicePort orderServicePort) {
    this.orderServicePort = orderServicePort;
  }

  @PostMapping
  public ResponseEntity<OrderDTO> create(@RequestBody CreateOrderRequestDTO request) {
    // TODO: map to domain. Criado em: 03/08/2024 ás 07:03:59.
    //Order order = orderServicePort.create(request.getCustomer(), request.getCollaborator());
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

}

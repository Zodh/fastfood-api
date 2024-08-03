package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.core.application.ports.repository.OrderRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.aggregate.ServiceAggregate;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.model.person.Collaborator;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.OrderServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServicePortImpl implements OrderServicePort {

  private final OrderRepositoryPort orderRepositoryPort;

  @Autowired
  public OrderServicePortImpl(OrderRepositoryPort orderRepositoryPort) {
    this.orderRepositoryPort = orderRepositoryPort;
  }

  @Override
  public Order create(Customer customer, Collaborator collaborator) {
    ServiceAggregate serviceAggregate = new ServiceAggregate(customer);
    Order order = serviceAggregate.createOrder(collaborator);
    return orderRepositoryPort.save(order);
  }
}

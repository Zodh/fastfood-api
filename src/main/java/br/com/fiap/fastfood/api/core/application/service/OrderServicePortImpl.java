package br.com.fiap.fastfood.api.core.application.service;

import br.com.fiap.fastfood.api.adapters.driven.infrastructure.repository.person.CollaboratorRepository;
import br.com.fiap.fastfood.api.core.application.exception.ApplicationException;
import br.com.fiap.fastfood.api.core.application.exception.NotFoundException;
import br.com.fiap.fastfood.api.core.application.ports.repository.OrderRepositoryPort;
import br.com.fiap.fastfood.api.core.domain.aggregate.ServiceAggregate;
import br.com.fiap.fastfood.api.core.domain.model.order.Order;
import br.com.fiap.fastfood.api.core.domain.model.person.Collaborator;
import br.com.fiap.fastfood.api.core.domain.model.person.Customer;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.CustomerServicePort;
import br.com.fiap.fastfood.api.core.domain.ports.inbound.OrderServicePort;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServicePortImpl implements OrderServicePort {

  private final OrderRepositoryPort orderRepositoryPort;
  private final CustomerServicePort customerServicePort;

  @Autowired
  public OrderServicePortImpl(OrderRepositoryPort orderRepositoryPort, CustomerServicePort customerServicePort) {
    this.orderRepositoryPort = orderRepositoryPort;
    this.customerServicePort = customerServicePort;
  }

  @Override
  public Order create(Customer customer, Collaborator collaborator) {
    customer = fetchCustomerData(customer);
    ServiceAggregate serviceAggregate = new ServiceAggregate(customer);
    Order order = serviceAggregate.createOrder(collaborator);
    return orderRepositoryPort.save(order);
  }

  private @Nullable Customer fetchCustomerData(Customer customer) {
    if (Objects.nonNull(customer)) {
      if (Objects.isNull(customer.getId()) && Objects.isNull(customer.getDocument())) {
        throw new ApplicationException("O identificador ou o documento do cliente precisa ser informado para constar no pedido!", "Cliente sem identificadores válidos!");
      }
      if (Objects.nonNull(customer.getId())) {
        customer = customerServicePort.getById(customer.getId());
      }
      if (Objects.nonNull(customer.getDocument())) {
        customer = customerServicePort.getByDocument(customer.getDocument());
      }
    }
    return customer;
  }
}
